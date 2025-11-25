@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package dev.gaddal.chat.data.network

import dev.gaddal.chat.data.dto.websocket.WebSocketMessageDto
import dev.gaddal.chat.data.lifecycle.AppLifecycleObserver
import dev.gaddal.chat.domain.models.ConnectionState
import dev.gaddal.core.data.networking.UrlConstants
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.logging.ChirpLogger
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import dev.gaddal.feature.chat.data.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration.Companion.seconds

/**
 * Responsible for managing WebSocket connections using the Ktor library. This class facilitates establishing,
 * maintaining, and retrying WebSocket connections while observing application lifecycle and network connectivity
 * states.
 *
 * The `KtorWebSocketConnector` integrates various components, such as session storage, error handlers, retry
 * mechanisms, and application lifecycle observers, to provide a robust mechanism for handling WebSocket-based
 * communication.
 *
 * The class exposes connection states and messages as reactive flows, allowing subscribers to monitor and react
 * to changes in the WebSocket connection and received messages in real time.
 *
 * @property httpClient The Ktor `HttpClient` used for creating WebSocket sessions.
 * @property applicationScope The `CoroutineScope` within which the WebSocket operations are confined.
 * @property sessionStorage The session storage used to observe and manage authentication information.
 * @property json The JSON serializer/deserializer used for encoding and decoding WebSocket messages.
 * @property connectionErrorHandler Handles errors specific to WebSocket connections and transforms them into
 * relevant domain-specific exceptions or states.
 * @property connectionRetryHandler Manages retry logic for reconnecting the WebSocket session upon failures.
 * @property appLifecycleObserver Observes the foreground or background state of the application to manage
 * WebSocket states.
 * @property connectivityObserver Observes the device network connectivity to determine whether the WebSocket
 * should be connected or disconnected.
 * @property logger A logger instance for debugging, error reporting, and state observation within the connector.
 */
class KtorWebSocketConnector(
    private val httpClient: HttpClient,
    private val applicationScope: CoroutineScope,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val connectionErrorHandler: ConnectionErrorHandler,
    private val connectionRetryHandler: ConnectionRetryHandler,
    private val appLifecycleObserver: AppLifecycleObserver,
    private val connectivityObserver: ConnectivityObserver,
    private val logger: ChirpLogger
) {
    /**
     * Represents the internal state of the WebSocket connection as a mutable state flow.
     *
     * The `_connectionState` variable is used to track and update the current connection status,
     * providing real-time updates on the network or WebSocket connection state. It initially starts
     * with the `DISCONNECTED` state and can transition through other states, such as `CONNECTING`,
     * `CONNECTED`, `ERROR_NETWORK`, or `ERROR_UNKNOWN`, based on the connection lifecycle and error handling.
     *
     * This variable is primarily used internally within the containing class to manage and emit
     * connection state changes and is not exposed directly to external consumers.
     */
    private val _connectionState = MutableStateFlow(ConnectionState.DISCONNECTED)

    /**
     * Represents a public read-only flow providing updates on the current WebSocket connection state.
     *
     * This flow emits values of type `ConnectionState`, indicating the current state
     * of the WebSocket connection, such as `CONNECTED`, `DISCONNECTED`, or `CONNECTING`.
     * It is a state flow, so it always has a current value and re-emits the latest state to new collectors.
     *
     * The connection state is derived and maintained internally, reflecting changes in the WebSocket
     * lifecycle or failure events.
     */
    val connectionState = _connectionState.asStateFlow()

    /**
     * Represents the current WebSocket session for ongoing communication with the server.
     *
     * This variable is used to maintain the active WebSocket connection and handle incoming and outgoing frames
     * during the session lifecycle. It is nullable to signify the absence of an active session.
     *
     * When a WebSocket connection is successfully established, this variable is assigned the session instance.
     * If the connection is closed or an error occurs, it is set to null to indicate disconnection.
     *
     * Scope:
     * - Provides state management for the WebSocket session.
     * - Enables sending and receiving messages between the client and the server.
     *
     * Thread Safety:
     * - This property is not thread-safe and should only be accessed or modified within synchronized contexts
     *   or through coroutines running on the same dispatcher.
     *
     * Invariant:
     * - This value should always be null when no WebSocket connection is established.
     */
    private var currentSession: WebSocketSession? = null

    /**
     * Represents a state flow indicating the current network connectivity status.
     *
     * This variable observes the `isConnected` Flow from the `ConnectivityObserver` class,
     * applies a 1-second debounce to avoid rapid state changes, and shares its state
     * within the application scope for efficient subscription and emission.
     *
     * The initial value of the flow is `false`, meaning it assumes a disconnected state
     * until connectivity status is actively observed and updated.
     */
    private val isConnected = connectivityObserver
        .isConnected
        .debounce(1.seconds)
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000L),
            false
        )

    /**
     * A Flow<Boolean> representing whether the application is currently in the foreground.
     * This property observes the state provided by the `appLifecycleObserver` and updates its
     * value accordingly. When the application moves to the foreground, a reset is triggered
     * on the `connectionRetryHandler` to skip any backoff delays for retry attempts.
     *
     * This state is shared within the `applicationScope` and remains active while there are subscribers.
     * The initial state is set to `false`, indicating the application is not in the foreground by default.
     */
    private val isInForeground = appLifecycleObserver
        .isInForeground
        .onEach { isInForeground ->
            if (isInForeground) {
                connectionRetryHandler.resetDelay()
            }
        }
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )

    /**
     * Represents a reactive flow of messages managed by a WebSocket connection,
     * dynamically adjusting based on authentication, network connectivity,
     * and application lifecycle state.
     *
     * This flow combines multiple dependencies to determine and maintain the
     * current state of WebSocket communication:
     *
     * - Observes authentication information via `observeAuthInfo`.
     * - Tracks network connectivity status through `isConnected`.
     * - Monitors whether the application is in the foreground via `isInForeground`.
     *
     * State Management:
     * - If there is no authentication information available, it clears the session and disconnects.
     * - Disconnects the WebSocket when the app is in the background.
     * - Sets appropriate connection states like `DISCONNECTED`, `CONNECTING`, and `ERROR_NETWORK`
     *   based on the network and session status.
     *
     * Error and Retry Handling:
     * - Handles WebSocket exceptions by transforming them into platform-compatible errors.
     * - Includes retry logic with exponential backoff and configurable conditions for whether
     *   retries should be attempted.
     * - Manages non-retriable errors gracefully, updating the connection state accordingly.
     *
     * Authenticated connections initiate a flow of WebSocket messages, automatically handling
     * pings, processing text frames, and handling disconnections.
     */
    val messages = combine(
        sessionStorage.observeAuthInfo(),
        isConnected,
        isInForeground
    ) { authInfo, isConnected, isInForeground ->
        when {
            authInfo == null -> {
                logger.info("No authentication details. Clearing session and disconnecting...")
                _connectionState.value = ConnectionState.DISCONNECTED
                currentSession?.close()
                currentSession = null
                connectionRetryHandler.resetDelay()
                null
            }

            !isInForeground -> {
                logger.info("App in background, disconnecting socket proactively.")
                _connectionState.value = ConnectionState.DISCONNECTED
                currentSession?.close()
                currentSession = null
                null
            }

            !isConnected -> {
                logger.info("Device is disconnected, closing WebSocket connection.")
                _connectionState.value = ConnectionState.ERROR_NETWORK
                currentSession?.close()
                currentSession = null
                null
            }

            else -> {
                logger.info("App in foreground & connected. Establishing connection...")

                if (_connectionState.value !in listOf(
                        ConnectionState.CONNECTING,
                        ConnectionState.CONNECTED
                    )
                ) {
                    _connectionState.value = ConnectionState.CONNECTING
                }

                authInfo
            }
        }
    }.flatMapLatest { authInfo ->
        if (authInfo == null) {
            emptyFlow()
        } else {
            createWebSocketFlow(authInfo.accessToken)
                // Catch block to transform exceptions for platform compatibility
                .catch { e ->
                    logger.error("Exception in WebSocket", e)

                    currentSession?.close()
                    currentSession = null

                    val transformedException = connectionErrorHandler.transformException(e)
                    throw transformedException
                }
                .retryWhen { t, attempt ->
                    logger.info("Connection failed on attempt $attempt")

                    val shouldRetry = connectionRetryHandler.shouldRetry(t, attempt)

                    if (shouldRetry) {
                        _connectionState.value = ConnectionState.CONNECTING
                        connectionRetryHandler.applyRetryDelay(attempt)
                    }

                    shouldRetry
                }
                // Catch block for non-retriable errors
                .catch { e ->
                    logger.error("Unhandled WebSocket error", e)

                    _connectionState.value = connectionErrorHandler.getConnectionStateForError(e)
                }
        }
    }

    /**
     * Creates a flow that handles WebSocket communication, facilitating the connection and message exchange
     * with a WebSocket server. Manages the connection state and processes incoming frames from the server,
     * including handling text messages and ping-pong frames.
     *
     * @param accessToken The access token used for authentication to the WebSocket server.
     *                    This token is included in the HTTP header as a bearer token to
     *                    authorize the connection.
     */
    private fun createWebSocketFlow(accessToken: String) = callbackFlow {
        _connectionState.value = ConnectionState.CONNECTING

        currentSession = httpClient.webSocketSession(
            urlString = "${UrlConstants.BASE_URL_WS}/chat"
        ) {
            header("Authorization", "Bearer $accessToken")
            header("X-API-Key", BuildKonfig.API_KEY)
        }

        currentSession?.let { session ->
            _connectionState.value = ConnectionState.CONNECTED

            session
                .incoming
                .consumeAsFlow()
                .buffer(
                    capacity = 100
                )
                .collect { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            logger.info("Received raw text frame: $text")

                            val messageDto = json.decodeFromString<WebSocketMessageDto>(text)
                            send(messageDto)
                        }

                        is Frame.Ping -> {
                            logger.debug("Received ping from server. Sending pong...")
                            session.send(Frame.Pong(frame.data))
                        }

                        else -> Unit
                    }
                }
        } ?: throw Exception("Failed to establish WebSocket connection")

        awaitClose {
            launch(NonCancellable) {
                logger.info("Disconnecting from WebSocket session...")
                _connectionState.value = ConnectionState.DISCONNECTED
                currentSession?.close()
                currentSession = null
            }
        }
    }

    /**
     * Sends a WebSocket message using the current session if the connection is active.
     *
     * This method ensures that the WebSocket connection is established and connected
     * before attempting to send a message. If the connection is not active or an error
     * occurs while sending the message, it returns an appropriate failure result.
     *
     * @param message The message to be sent over the WebSocket.
     * @return An `EmptyResult` representing the outcome of the operation:
     *         - `Result.Success` if the message was successfully sent.
     *         - `Result.Failure` with `DataError.Connection.NOT_CONNECTED` if the WebSocket is not connected.
     *         - `Result.Failure` with `DataError.Connection.MESSAGE_SEND_FAILED` if the message could not be sent due to an error.
     */
    suspend fun sendMessage(message: String): EmptyResult<DataError.Connection> {
        val connectionState = connectionState.value

        if (currentSession == null || connectionState != ConnectionState.CONNECTED) {
            return Result.Failure(DataError.Connection.NOT_CONNECTED)
        }

        return try {
            currentSession?.send(message)
            Result.Success(Unit)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            logger.error("Unable to send WebSocket message", e)
            Result.Failure(DataError.Connection.MESSAGE_SEND_FAILED)
        }
    }
}