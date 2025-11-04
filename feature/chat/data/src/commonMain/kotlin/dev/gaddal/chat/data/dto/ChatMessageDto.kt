package dev.gaddal.chat.data.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data transfer object (DTO) for a chat message, used for serialization and
 * communication between client and server in chat-related operations.
 *
 * This class encapsulates the details of a chat message, including its unique identifier,
 * the chat it belongs to, the message content, the timestamp of creation,
 * and the unique identifier of the sender.
 *
 * @property id The unique identifier of the message.
 * @property chatId The unique identifier of the chat this message is associated with.
 * @property content The textual content of the message.
 * @property createdAt The timestamp representing when the message was created.
 * @property senderId The unique identifier of the sender of the message.
 */
@Serializable
data class ChatMessageDto(
    val id: String,
    val chatId: String,
    val content: String,
    val createdAt: String,
    val senderId: String
)