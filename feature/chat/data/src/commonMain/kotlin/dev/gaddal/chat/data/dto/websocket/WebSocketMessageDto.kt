package dev.gaddal.chat.data.dto.websocket

import kotlinx.serialization.Serializable

/**
 * Represents a data transfer object for WebSocket messages.
 *
 * This class is used to encapsulate the type and payload of messages exchanged over a WebSocket connection.
 * The `type` field specifies the type of the message, allowing the client or server to determine how the
 * `payload` should be processed. The `payload` field contains the actual content or data of the message.
 *
 * @property type The type of the WebSocket message as a string. Typically used to identify the nature
 *                or purpose of the message.
 * @property payload The serialized content or data of the WebSocket message.
 */
@Serializable
data class WebSocketMessageDto(
    val type: String,
    val payload: String
)