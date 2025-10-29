package dev.gaddal.core.data.dto.requests

import kotlinx.serialization.Serializable

/**
 * Data class representing the request body for user login.
 *
 * This class encapsulates the necessary information required to authenticate a user,
 * including the email and password. It is typically serialized and sent as part of
 * the HTTP request body to initiate an authentication process.
 *
 * The `LoginRequest` is designed to be used with authentication services and routes
 * where user login credentials are validated to establish session tokens or other
 * forms of authenticated access.
 */
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)