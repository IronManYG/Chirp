package dev.gaddal.chat.data.mappers

import dev.gaddal.chat.data.dto.ChatParticipantDto
import dev.gaddal.chat.domain.models.ChatParticipant

/**
 * Converts an instance of `ChatParticipantDto` to its corresponding domain model `ChatParticipant`.
 *
 * This method maps the properties of the `ChatParticipantDto` data transfer object to the
 * `ChatParticipant` domain model, preserving the user's unique identifier, display name,
 * and optional profile picture URL.
 *
 * @return The domain model `ChatParticipant` derived from the current `ChatParticipantDto`.
 */
fun ChatParticipantDto.toDomain(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}