package dev.gaddal.chat.data.dto.response

import kotlinx.serialization.Serializable

/**
 * Represents the response for profile picture upload URLs.
 *
 * This data class encapsulates information about the URLs required for uploading
 * and accessing a profile picture, as well as any associated headers needed
 * for the upload process.
 *
 * @property uploadUrl The URL to which the profile picture should be uploaded.
 * @property publicUrl The public URL to access the uploaded profile picture.
 * @property headers A map of HTTP headers required for uploading the profile picture.
 */
@Serializable
data class ProfilePictureUploadUrlsResponse(
    val uploadUrl: String,
    val publicUrl: String,
    val headers: Map<String, String>
)