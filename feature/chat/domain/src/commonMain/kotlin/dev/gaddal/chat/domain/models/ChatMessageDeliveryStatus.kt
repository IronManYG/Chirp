package dev.gaddal.chat.domain.models

/**
 * Represents the delivery status of a chat message within a conversation.
 *
 * This enum is used to track and represent the state of a message's delivery process.
 *
 * - SENDING: The message is in the process of being sent.
 * - SENT: The message has been successfully sent to the recipient.
 * - FAILED: The message could not be delivered due to an error.
 */
enum class ChatMessageDeliveryStatus {
    SENDING,
    SENT,
    FAILED
}