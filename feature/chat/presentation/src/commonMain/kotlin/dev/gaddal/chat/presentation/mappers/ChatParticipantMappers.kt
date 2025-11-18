package dev.gaddal.chat.presentation.mappers

import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.core.designsystem.components.avatar.ChatParticipantUi
import dev.gaddal.core.domain.auth.User

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

/**
 * Maps a [User] domain model to its UI representation [ChatParticipantUi].
 *
 * The function creates a UI-friendly representation of a user by converting their
 * identifier, username, and profile picture URL. If a profile picture URL is not
 * provided, the user's initials are generated using the first two characters
 * of their username, transformed to uppercase.
 *
 * @return A [ChatParticipantUi] instance containing the ID, username, initials, and
 * optional profile picture URL for the user.
 */
fun User.toUi(): ChatParticipantUi {
    return ChatParticipantUi(
        id = id,
        username = username,
        initials = username.take(2).uppercase(),
        imageUrl = profilePictureUrl
    )
}