package dev.gaddal.chat.data.mappers

import dev.gaddal.chat.data.dto.response.ProfilePictureUploadUrlsResponse
import dev.gaddal.chat.domain.models.ProfilePictureUploadUrls

/**
 * Converts the `ProfilePictureUploadUrlsResponse` data transfer object to its corresponding domain model
 * `ProfilePictureUploadUrls`.
 *
 * This function maps the properties of the `ProfilePictureUploadUrlsResponse`, such as the upload URL,
 * public URL, and headers, to the equivalent properties in the `ProfilePictureUploadUrls` domain model.
 *
 * @return A new instance of `ProfilePictureUploadUrls` derived from the `ProfilePictureUploadUrlsResponse`.
 */
fun ProfilePictureUploadUrlsResponse.toDomain(): ProfilePictureUploadUrls {
    return ProfilePictureUploadUrls(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers
    )
}