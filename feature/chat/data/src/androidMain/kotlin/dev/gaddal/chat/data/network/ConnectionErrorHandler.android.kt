package dev.gaddal.chat.data.network

import dev.gaddal.chat.domain.models.ConnectionState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.network.sockets.SocketTimeoutException
import kotlinx.io.EOFException
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

/**
 * Handles errors related to connection issues within the application.
 *
 * This class provides utility functions to determine the state of a connection based on
 * a throwable cause, transform exceptions, and identify retriable errors. It is designed
 * to standardize the process of handling connection-related errors.
 */
actual class ConnectionErrorHandler {
    /**
     * Determines the connection state based on the provided error cause.
     *
     * This method maps specific throwable instances related to network issues
     * to a corresponding `ConnectionState`. For known network-related errors,
     * it returns `ConnectionState.ERROR_NETWORK`.
     * For any other exception, it returns `ConnectionState.ERROR_UNKNOWN`.
     *
     * @param cause The throwable that caused the connection error. This can include
     * network-related exceptions like `ClientRequestException`, `WebSocketException`,
     * `SocketException`, `SocketTimeoutException`, `UnknownHostException`,
     * `SSLException`, or `EOFException`.
     * @return The appropriate `ConnectionState` for the error, either
     * `ConnectionState.ERROR_NETWORK` for network issues or
     * `ConnectionState.ERROR_UNKNOWN` for other errors.
     */
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        return when (cause) {
            is ClientRequestException,
            is WebSocketException,
            is SocketException,
            is SocketTimeoutException,
            is UnknownHostException,
            is SSLException,
            is EOFException -> ConnectionState.ERROR_NETWORK

            else -> ConnectionState.ERROR_UNKNOWN
        }
    }

    /**
     * Transforms the given exception and returns a modified or unchanged version of it.
     *
     * This function can be used to process exceptions, apply any transformations or mappings
     * as deemed necessary, and then return either the transformed exception or the original one.
     *
     * @param exception The exception to be transformed.
     * @return The transformed exception or the original exception if no transformations are applied.
     */
    actual fun transformException(exception: Throwable): Throwable {
        return exception
    }

    /**
     * Determines if a given error is considered retriable based on its type.
     *
     * This function checks the type of the provided Throwable and returns `true`
     * for specific error types that are deemed retriable, such as:
     * - SocketTimeoutException
     * - WebSocketException
     * - SocketException
     * - EOFException
     *
     * All other error types are considered non-retriable, and `false` is returned.
     *
     * @param cause The Throwable instance representing the error to be evaluated.
     * @return A Boolean value indicating whether the error is retriable (`true`) or not (`false`).
     */
    actual fun isRetriableError(cause: Throwable): Boolean {
        return when (cause) {
            is SocketTimeoutException,
            is WebSocketException,
            is SocketException,
            is EOFException -> true

            else -> false
        }
    }
}