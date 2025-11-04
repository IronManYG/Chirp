package dev.gaddal.chat.data.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data transfer object (DTO) for a chat, used for serialization and communication
 * between client and server in chat-related operations.
 *
 * This class encapsulates the essential details of a chat, including its unique identifier,
 * list of participants, timestamp of the last activity, and the most recent message, if available.
 *
 * @property id The unique identifier of the chat.
 * @property participants The list of participants involved in the chat.
 * @property lastActivityAt The formatted timestamp of the chat's last activity.
 * @property lastMessage The last message in the chat, or null if no messages exist.
 */
@Serializable
data class ChatDto(
    val id: String,
    val participants: List<ChatParticipantDto>,
    val lastActivityAt: String,
    val lastMessage: ChatMessageDto?
)