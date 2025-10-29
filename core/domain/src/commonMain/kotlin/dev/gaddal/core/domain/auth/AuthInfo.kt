package dev.gaddal.core.domain.auth

/**
 * Represents authentication information for a user, including tokens and user details.
 *
 * This data class is used to store details returned from authentication operations,
 * such as login responses, and includes the following information:
 *
 * - `accessToken`: The token used for accessing secure resources on behalf of the user.
 * - `refreshToken`: The token used to refresh the access token when it expires.
 * - `user`: An instance of the `User` class containing user-specific information.
 */
data class AuthInfo(
    val accessToken: String,
    val refreshToken: String,
    val user: User
)