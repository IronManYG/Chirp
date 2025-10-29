package dev.gaddal.core.domain.auth

/**
 * Represents a user in the authentication system.
 *
 * This data class encapsulates information about a user, including their
 * unique identifier, email address, username, email verification status,
 * and an optional profile picture URL. It serves as the core representation
 * of a user within the domain model.
 *
 * @property id The unique identifier for the user.
 * @property email The email address associated with the user's account.
 * @property username The chosen username of the user.
 * @property hasVerifiedEmail Indicates whether the email address has been verified.
 * @property profilePictureUrl The URL of the user's profile picture, if available.
 */
data class User(
    val id: String,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
    val profilePictureUrl: String? = null
)