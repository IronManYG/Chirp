package dev.gaddal.core.data.dto.requests

import kotlinx.serialization.Serializable

/**
 * Data class representing a request to reset a user's password.
 *
 * This class encapsulates the necessary information for a password reset operation,
 * which typically includes the new password and the token issued for verification purposes.
 *
 * Instances of this class are expected to be serialized and sent as part of the
 * HTTP request body to the appropriate endpoint responsible for handling password reset processes.
 */
@Serializable
data class ResetPasswordRequest(
    val newPassword: String,
    val token: String
)