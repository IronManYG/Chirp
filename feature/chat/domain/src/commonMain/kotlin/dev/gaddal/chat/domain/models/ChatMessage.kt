package dev.gaddal.chat.domain.models

import kotlin.time.Instant

/**
 * Represents a message within a chat, including its content, association to a specific chat,
 * and metadata about its creation and sender.
 *
 * @property id The unique identifier of the message.
 * @property chatId The unique identifier of the chat this message belongs to.
 * @property content The textual content of the message.
 * @property createdAt The timestamp indicating when the message was created.
 * @property senderId The unique identifier of the sender who authored the message.
 */
data class ChatMessage(
    val id: String,
    val chatId: String,
    val content: String,
    val createdAt: Instant,
    val senderId: String
)