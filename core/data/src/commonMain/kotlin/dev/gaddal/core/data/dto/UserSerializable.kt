package dev.gaddal.core.data.dto

import kotlinx.serialization.Serializable

/**
 * Represents a serializable user model for data transfer.
 *
 * This data class is primarily used for serializing and deserializing user-related data when communicating
 * with external systems, such as APIs or databases. It serves as a lightweight representation of the user
 * entity containing essential identification and profile information, along with an email verification status.
 *
 * @property id The unique identifier associated with the user.
 * @property email The email address of the user.
 * @property username The username chosen by the user.
 * @property hasVerifiedEmail A flag indicating whether the user's email address has been verified.
 * @property profilePictureUrl The URL of the user's optional profile picture, or null if not available.
 */
@Serializable
data class UserSerializable(
    val id: String,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
    val profilePictureUrl: String? = null
)