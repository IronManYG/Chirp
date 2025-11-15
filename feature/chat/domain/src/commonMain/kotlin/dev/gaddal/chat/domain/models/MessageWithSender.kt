package dev.gaddal.chat.domain.models

/**
 * Represents a message exchanged in a chat along with its sender and delivery status.
 *
 * @property message The chat message containing its content, metadata, and association to a chat.
 * @property sender The participant who sent the message, including their user details.
 * @property deliveryStatus The current delivery status of the message, if applicable.
 */
data class MessageWithSender(
    val message: ChatMessage,
    val sender: ChatParticipant,
    val deliveryStatus: ChatMessageDeliveryStatus?
)