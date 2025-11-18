package dev.gaddal.chat.data.mappers

import dev.gaddal.chat.data.dto.ChatParticipantDto
import dev.gaddal.chat.database.entities.ChatParticipantEntity
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

/**
 * Converts the current `ChatParticipantEntity` instance to its corresponding domain model `ChatParticipant`.
 *
 * This function maps the properties of the `ChatParticipantEntity`, such as the user's unique identifier,
 * display name, and optional profile picture URL, to the equivalent properties in the `ChatParticipant` domain model.
 *
 * @return The domain model `ChatParticipant` instance derived from the current `ChatParticipantEntity`.
 */
fun ChatParticipantEntity.toDomain(): ChatParticipant {
    return ChatParticipant(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}

/**
 * Converts the current `ChatParticipant` instance to its corresponding database entity `ChatParticipantEntity`.
 *
 * Maps the properties of the `ChatParticipant` domain model, such as the unique user ID, username,
 * and optional profile picture URL, into the equivalent properties of the `ChatParticipantEntity` database entity.
 *
 * @return The `ChatParticipantEntity` database representation derived from the current `ChatParticipant` instance.
 */
fun ChatParticipant.toEntity(): ChatParticipantEntity {
    return ChatParticipantEntity(
        userId = userId,
        username = username,
        profilePictureUrl = profilePictureUrl
    )
}