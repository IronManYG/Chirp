package dev.gaddal.chat.domain.models

/**
 * Encapsulates information about a chat, including basic chat details and associated messages.
 *
 * @property chat The chat associated with the information, containing its details such as participants,
 * latest activity, and the latest message.
 * @property messages A list of messages exchanged in the chat along with sender information and delivery status.
 */
data class ChatInfo(
    val chat: Chat,
    val messages: List<MessageWithSender>
)