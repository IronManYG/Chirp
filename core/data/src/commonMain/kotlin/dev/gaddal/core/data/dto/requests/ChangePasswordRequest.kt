package dev.gaddal.core.data.dto.requests

import kotlinx.serialization.Serializable

/**
 * Data class representing a request to change a user's password.
 *
 * This class encapsulates the necessary information for changing a password,
 * specifically the user's current password and the new desired password.
 *
 * Instances of this class are typically serialized and sent as part of the
 * HTTP request body to the endpoint handling password change operations.
 */
@Serializable
data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)