package dev.gaddal.core.data.dto.requests

import kotlinx.serialization.Serializable

/**
 * Data class representing a request to send an email-related operation, such as resending a
 * verification email or performing any other email-based action.
 *
 * This class encapsulates the email information required for such operations and
 * is typically serialized and used as part of the HTTP request body.
 */
@Serializable
data class EmailRequest(
    val email: String
)