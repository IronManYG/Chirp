package dev.gaddal.chat.domain.models

/**
 * Represents a new outgoing message that is being sent in a chat.
 *
 * @property chatId The unique identifier of the chat where the message is being sent.
 * @property messageId The unique identifier for the new outgoing message.
 * @property content The textual content of the message being sent.
 */
data class OutgoingNewMessage(
    val chatId: String,
    val messageId: String,
    val content: String
)