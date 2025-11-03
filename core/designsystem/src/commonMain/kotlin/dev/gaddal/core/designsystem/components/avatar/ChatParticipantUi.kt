package dev.gaddal.core.designsystem.components.avatar

/**
 * Represents a user avatar with associated information, such as an identifier, username,
 * user initials, and an optional avatar image URL.
 *
 * @property id A unique identifier for the avatar.
 * @property username The name of the user associated with the avatar.
 * @property initials Initials displayed for the user if no image is available.
 * @property imageUrl An optional URL for the avatar image. If `null`, initials are displayed instead.
 */
data class ChatParticipantUi(
    val id: String,
    val username: String,
    val initials: String,
    val imageUrl: String? = null
)