package dev.gaddal.chat.presentation.mappers

import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.core.designsystem.components.avatar.ChatParticipantUi

/**
 * Converts a [ChatParticipant] instance into a [ChatParticipantUi] instance for use in the UI layer.
 *
 * This transformation maps the core participant details such as user ID, username, initials,
 * and profile picture URL from the domain model to the corresponding properties in the UI model.
 *
 * @return A [ChatParticipantUi] instance containing the mapped user details for the UI layer.
 */
fun ChatParticipant.toUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = userId,
        username = username,
        initials = initials,
        imageUrl = profilePictureUrl
    )
}