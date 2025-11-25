package dev.gaddal.chat.domain.chat

import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.chat.domain.models.ConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

/**
 * Defines a client for managing chat connections and interactions.
 *
 * This interface allows observing chat messages and monitoring connection states
 * within a connected environment. The client ensures
 * that chat operations are performed in a consistent and asynchronous manner.
 */
interface ChatConnectionClient {
    /**
     * A flow of `ChatMessage` objects that represents a stream of real-time chat messages.
     *
     * Each emitted `ChatMessage` contains details about the message, including its content,
     * sender, associated chat, timestamp, and delivery status. This flow allows subscribers
     * to listen to and process incoming chat messages in a reactive manner.
     *
     * This is typically used to monitor and process messages in the context of a connected chat client.
     */
    val chatMessages: Flow<ChatMessage>

    /**
     * A state flow that emits updates regarding the current connection status.
     *
     * This state flow represents the connection state of the application or service,
     * and provides updates as the connection status changes. The value emitted by
     * this state flow is of type [ConnectionState], which describes various states
     * such as disconnected, connecting, connected, and error states.
     *
     * Observers can collect this flow to react to changes in connection status and
     * update the user interface or execute logic accordingly. The states included
     * in [ConnectionState] are:
     * - `DISCONNECTED`: No active connection is established.
     * - `CONNECTING`: Attempting to establish a connection.
     * - `CONNECTED`: The connection is successfully established.
     * - `ERROR_NETWORK`: A network-related error occurred.
     * - `ERROR_UNKNOWN`: An unidentified error occurred during the connection process.
     */
    val connectionState: StateFlow<ConnectionState>
}