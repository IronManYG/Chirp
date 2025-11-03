package dev.gaddal.chat.data.dto

import kotlinx.serialization.Serializable

/**
 * Represents a data transfer object (DTO) for a chat participant, used for serialization and
 * communication between client and server in chat-related operations.
 *
 * This class encapsulates the necessary details of a chat participant, including their unique
 * identifier, display name, and optional profile picture URL.
 *
 * @property userId The unique identifier of the participant.
 * @property username The display name of the participant.
 * @property profilePictureUrl The URL of the participant's profile picture, or null if not provided.
 */
@Serializable
data class ChatParticipantDto(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?
)