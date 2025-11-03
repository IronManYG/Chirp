package dev.gaddal.chat.domain.models

/**
 * Represents a participant in a chat, containing identifying user details and optional profile information.
 *
 * @property userId The unique identifier of the user.
 * @property username The display name of the user in the chat.
 * @property profilePictureUrl The URL to the user's profile picture, or null if not provided.
 */
data class ChatParticipant(
    val userId: String,
    val username: String,
    val profilePictureUrl: String?
) {
    /**
     * Provides the uppercase initials derived from the first two characters of the username.
     *
     * The initials are computed by taking the first two characters of the `username` property,
     * converting them to uppercase. If the `username` is less than two characters, the available
     * characters are used.
     */
    val initials: String
        get() = username.take(2).uppercase()
}