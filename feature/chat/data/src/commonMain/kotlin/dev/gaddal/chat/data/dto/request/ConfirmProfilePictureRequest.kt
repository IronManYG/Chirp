package dev.gaddal.chat.data.dto.request

import kotlinx.serialization.Serializable

/**
 * Represents a request to confirm the profile picture upload.
 *
 * This data class encapsulates the required information to confirm that a profile picture
 * has been successfully uploaded to the server. It is used to finalize the process of updating
 * or adding a user's profile picture in the chat system.
 *
 * @property publicUrl The publicly accessible URL of the uploaded profile picture,
 * which is provided by the server after the upload.
 */
@Serializable
data class ConfirmProfilePictureRequest(
    val publicUrl: String
)