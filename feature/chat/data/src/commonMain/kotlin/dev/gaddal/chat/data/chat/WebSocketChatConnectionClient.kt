package dev.gaddal.chat.data.chat

import dev.gaddal.chat.data.dto.websocket.WebSocketMessageDto
import dev.gaddal.chat.data.mappers.toNewMessage
import dev.gaddal.chat.data.network.KtorWebSocketConnector
import dev.gaddal.chat.database.ChirpChatDatabase
import dev.gaddal.chat.domain.chat.ChatConnectionClient
import dev.gaddal.chat.domain.chat.ChatRepository
import dev.gaddal.chat.domain.error.ConnectionError
import dev.gaddal.chat.domain.message.MessageRepository
import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.chat.domain.models.ChatMessageDeliveryStatus
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

/**
 * A client for managing WebSocket-based chat connections and message handling.
 *
 * This class provides functionalities to establish and maintain WebSocket connections,
 * send and receive chat messages, and synchronize message state with the local database
 * and chat repository.
 *
 * @property webSocketConnector The WebSocket connector used for managing the WebSocket connection.
 * @property chatRepository Handles chat-related data operations and interactions with the local repository.
 * @property database The local database instance used for storing and querying persistent data.
 * @property sessionStorage Manages session-related storage required during WebSocket interactions.
 * @property json JSON serializer/deserializer for encoding/decoding message payloads.
 * @property messageRepository Repository for managing message data and delivery status updates.
 */
class WebSocketChatConnectionClient(
    private val webSocketConnector: KtorWebSocketConnector,
    private val chatRepository: ChatRepository,
    private val database: ChirpChatDatabase,
    private val sessionStorage: SessionStorage,
    private val json: Json,
    private val messageRepository: MessageRepository
) : ChatConnectionClient {

    /**
     * A stream of chat messages represented as a [Flow] of [ChatMessage].
     *
     * This property provides a continuous stream of chat messages that
     * are either received from or sent to the connected chat server.
     * The messages encapsulate information such as content, sender,
     * timestamp, delivery status, and association to a specific chat.
     *
     * @see ChatMessage
     */
    override val chatMessages: Flow<ChatMessage>
        get() = TODO("Not yet implemented")

    /**
     * Represents the current state of the WebSocket connection.
     *
     * This property retrieves the connection state from the underlying `webSocketConnector`.
     * It allows monitoring of the connection's status, which can be used to determine
     * whether the WebSocket is connected, connecting, or disconnected.
     *
     * Possible states can include connection statuses such as connected, connecting, or disconnected,
     * depending on the implementation of `webSocketConnector.connectionState`.
     */
    override val connectionState = webSocketConnector.connectionState

    /**
     * Sends a chat message over a WebSocket connection and updates the message delivery status
     * in case of a failure.
     *
     * @param message The chat message to be sent, including its metadata and content.
     * @return An `EmptyResult` representing the result of the operation:
     *         - Success: The message was sent successfully.
     *         - Failure: Contains a `ConnectionError` indicating the reason for the failure.
     */
    override suspend fun sendChatMessage(message: ChatMessage): EmptyResult<ConnectionError> {
        val outgoingDto = message.toNewMessage()
        val webSocketMessage = WebSocketMessageDto(
            type = outgoingDto.type.name,
            payload = json.encodeToString(outgoingDto)
        )
        val rawJsonPayload = json.encodeToString(webSocketMessage)

        return webSocketConnector
            .sendMessage(rawJsonPayload)
            .onFailure { error ->
                messageRepository.updateMessageDeliveryStatus(
                    messageId = message.id,
                    status = ChatMessageDeliveryStatus.FAILED
                )
            }
    }
}