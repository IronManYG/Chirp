package dev.gaddal.chat.data.dto.websocket

import kotlinx.serialization.Serializable

/**
 * Enum representing the different types of outgoing WebSocket messages.
 *
 * This is used to categorize the purpose or intent of messages sent to the connected WebSocket client.
 */
enum class OutgoingWebSocketType {
    /**
     * Represents a specific type of outgoing WebSocket event indicating a new message.
     *
     * This enum value is used within the WebSocket communication system
     * to categorize and identify events related to the creation or dispatch
     * of new chat messages.
     */
    NEW_MESSAGE
}

/**
 * Represents a base class for outgoing WebSocket messages.
 *
 * This sealed class serves as a foundation for different types of outgoing WebSocket messages
 * by defining a common structure with the `type` property. Each subclass represents a specific
 * type of WebSocket message to be sent from the client or server.
 *
 * @param type The type of the outgoing WebSocket message, represented by the [OutgoingWebSocketType] enum.
 */
@Serializable
sealed class OutgoingWebSocketDto(
    val type: OutgoingWebSocketType
) {

    /**
     * Represents a new message to be sent through a WebSocket connection.
     *
     * This data class is used to encapsulate the details of a new message being transmitted from the
     * client to the server. It extends the [OutgoingWebSocketDto] class and specifies the type as
     * [OutgoingWebSocketType.NEW_MESSAGE].
     *
     * @property chatId The unique identifier of the chat to which the message belongs.
     * @property messageId The unique identifier of the message.
     * @property content The actual content of the message.
     */
    @Serializable
    data class NewMessage(
        val chatId: String,
        val messageId: String,
        val content: String
    ) : OutgoingWebSocketDto(OutgoingWebSocketType.NEW_MESSAGE)
}