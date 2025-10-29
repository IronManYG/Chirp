package dev.gaddal.core.data.dto.requests

import kotlinx.serialization.Serializable

/**
 * Data class representing the request payload for refreshing an authentication token.
 *
 * This class encapsulates the information necessary to request a new authentication token,
 * specifically the refresh token provided during the initial authentication process.
 *
 * The `RefreshRequest` is typically serialized and sent as part of the HTTP request body
 * to an endpoint responsible for token renewal. It is used to maintain user sessions
 * without requiring re-authentication by exchanging a valid refresh token for a new access token.
 */
@Serializable
data class RefreshRequest(
    val refreshToken: String
)