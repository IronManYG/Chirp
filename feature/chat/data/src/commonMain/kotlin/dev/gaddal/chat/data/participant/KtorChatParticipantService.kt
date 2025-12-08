package dev.gaddal.chat.data.participant

import dev.gaddal.chat.data.dto.ChatParticipantDto
import dev.gaddal.chat.data.dto.request.ConfirmProfilePictureRequest
import dev.gaddal.chat.data.dto.response.ProfilePictureUploadUrlsResponse
import dev.gaddal.chat.data.mappers.toDomain
import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.chat.domain.models.ProfilePictureUploadUrls
import dev.gaddal.chat.domain.participant.ChatParticipantService
import dev.gaddal.core.data.networking.delete
import dev.gaddal.core.data.networking.get
import dev.gaddal.core.data.networking.post
import dev.gaddal.core.data.networking.safeCall
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url

/**
 * An implementation of the ChatParticipantService utilizing Ktor's HttpClient.
 *
 * This service provides functionality to interact with chat participant-related endpoints
 * by leveraging an HTTP client for API communication.
 *
 * @constructor Creates an instance of KtorChatParticipantService with the provided HttpClient.
 * @param httpClient The HttpClient instance used for making HTTP requests to the chat participant API.
 */
class KtorChatParticipantService(
    private val httpClient: HttpClient
) : ChatParticipantService {

    /**
     * Searches for a chat participant based on the given query string.
     *
     * This method performs a network operation to search for a participant in the chat system
     * using the provided query. If successful, it returns a result containing the participant's details.
     * In case of failure, a corresponding error is returned.
     *
     * @param query The search query used to locate a specific chat participant.
     * @return A [dev.gaddal.core.domain.util.Result] containing either a [dev.gaddal.chat.domain.models.ChatParticipant] on success or a [dev.gaddal.core.domain.util.DataError.Remote] on failure.
     */
    override suspend fun searchParticipant(query: String): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantDto>(
            route = "/participants",
            queryParams = mapOf(
                "query" to query
            )
        ).map { it.toDomain() }
    }

    /**
     * Retrieves the local participant in the chat system.
     *
     * This method performs a network operation to fetch information about the participant
     * associated with the local client. The operation either returns the participant's details
     * or provides an error if the request fails.
     *
     * @return A [Result] containing either a [ChatParticipant] on success or a [DataError.Remote] on failure.
     */
    override suspend fun getLocalParticipant(): Result<ChatParticipant, DataError.Remote> {
        return httpClient.get<ChatParticipantDto>(
            route = "/participants"
        ).map { it.toDomain() }
    }

    /**
     * Generates a URL and related metadata for uploading a profile picture.
     *
     * This method performs a network operation to request a pre-signed upload URL, allowing the
     * client to upload a profile picture directly to the storage server. The resulting metadata contains
     * the upload URL, the public URL where the profile picture will be accessible once uploaded, and
     * any required headers for the upload operation.
     *
     * @param mimeType The MIME type of the profile picture file to be uploaded (e.g., "image/png", "image/jpeg").
     * @return A [Result] containing either a [ProfilePictureUploadUrls] on success or a [DataError.Remote] on failure.
     */
    override suspend fun getProfilePictureUploadUrl(mimeType: String): Result<ProfilePictureUploadUrls, DataError.Remote> {
        return httpClient.post<Unit, ProfilePictureUploadUrlsResponse>(
            route = "/participants/profile-picture-upload",
            queryParams = mapOf(
                "mimeType" to mimeType
            ),
            body = Unit
        ).map { it.toDomain() }
    }

    /**
     * Uploads a profile picture to the provided URL.
     *
     * This method performs a network operation to upload an image to the specified URL using the
     * provided image bytes and headers. The operation returns a result indicating success or failure.
     *
     * @param uploadUrl The URL to which the profile picture will be uploaded.
     * @param imageBytes The image content in the form of a byte array.
     * @param headers Additional headers to include in the request.
     * @return An [EmptyResult] that indicates success or a [DataError.Remote] in case of failure.
     */
    override suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote> {
        return safeCall {
            httpClient.put {
                url(uploadUrl)
                headers.forEach { (key, value) ->
                    header(key, value)
                }
                setBody(imageBytes)
            }
        }
    }

    /**
     * Confirms the upload of a profile picture using the provided public URL.
     *
     * This method performs a network operation to finalize the process
     * of uploading a profile picture by sending a confirmation request.
     * If the operation is successful, an empty result is returned.
     * Otherwise, a remote data error is provided.
     *
     * @param publicUrl The public URL of the uploaded profile picture.
     * @return An [EmptyResult] that is either successful or contains a [DataError.Remote].
     */
    override suspend fun confirmProfilePictureUpload(publicUrl: String): EmptyResult<DataError.Remote> {
        return httpClient.post<ConfirmProfilePictureRequest, Unit>(
            route = "/participants/confirm-profile-picture",
            body = ConfirmProfilePictureRequest(publicUrl)
        )
    }

    /**
     * Deletes the profile picture of the current participant.
     *
     * This method performs a network operation to remove the profile picture associated with the
     * local participant in the chat system. If the operation is successful, an empty result is
     * returned. Otherwise, it provides a remote data error.
     *
     * @return An [EmptyResult] that indicates success or contains a [DataError.Remote] in case of failure.
     */
    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/participants/profile-picture"
        )
    }
}