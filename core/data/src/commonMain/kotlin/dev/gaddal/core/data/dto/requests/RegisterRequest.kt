
package dev.gaddal.core.data.dto.requests

import kotlinx.serialization.Serializable

/**
 * Data class representing the request body for user registration.
 *
 * This class is used to encapsulate the necessary information required to
 * register a new user, including the email, username, and password.
 *
 * Instances of this class are typically serialized and sent as part of
 * the HTTP request body to the authentication service endpoint.
 */
@Serializable
data class RegisterRequest(
    val email: String,
    val username: String,
    val password: String
)