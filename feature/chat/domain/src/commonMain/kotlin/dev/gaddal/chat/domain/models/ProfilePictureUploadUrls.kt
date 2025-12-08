package dev.gaddal.chat.domain.models

/**
 * Represents the URLs and metadata required for uploading a user's profile picture.
 *
 * @property uploadUrl The URL to which the profile picture file should be uploaded.
 * @property publicUrl The publicly accessible URL of the uploaded profile picture after successful upload.
 * @property headers A map of headers required for the upload request, such as authorization or content type.
 */
data class ProfilePictureUploadUrls(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)