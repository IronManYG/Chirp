package dev.gaddal.chat.domain.models

import kotlin.time.Instant

/**
 * Represents a chat between participants with information about the latest activity and message.
 *
 * @property id The unique identifier of the chat.
 * @property participants A list of participants involved in the chat.
 * @property lastActivityAt The timestamp of the last activity in the chat.
 * @property lastMessage The content of the most recent message in the chat, or null if no messages have been sent.
 * @property lastMessageSenderUsername The username of the sender of the last message, or null if unavailable.
 */
data class Chat(
    val id: String,
    val participants: List<ChatParticipant>,
    val lastActivityAt: Instant,
    val lastMessage: ChatMessage?,
    val lastMessageSenderUsername: String? = null
)