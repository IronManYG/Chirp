package dev.gaddal.chat.domain.participant

import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.chat.domain.models.ProfilePictureUploadUrls
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result

/**
 * Service interface for managing chat participants.
 *
 * Provides functionality for searching for and retrieving participants in a chat.
 */
interface ChatParticipantService {
    /**
     * Searches for a participant in a chat based on the provided query.
     *
     * This method performs a search operation using the specified query string and returns the result.
     * The result can either be a successful match with a `ChatParticipant` or a failure due to a remote data error.
     *
     * @param query The query string used to search for the participant. It typically represents a username or related identifier.
     * @return A `Result` object containing either a `ChatParticipant` on success or a `DataError.Remote` on failure.
     */
    suspend fun searchParticipant(
        query: String
    ): Result<ChatParticipant, DataError.Remote>

    /**
     * Retrieves the local participant in a chat.
     *
     * This method is used to fetch the details of the participant representing the local user in the context of a chat.
     * The operation returns either a successful result containing the `ChatParticipant` instance or a failure
     * with a `DataError.Remote` indicating an issue with the remote data retrieval.
     *
     * @return A `Result` object containing either a `ChatParticipant` on success or a `DataError.Remote` on failure.
     */
    suspend fun getLocalParticipant(): Result<ChatParticipant, DataError.Remote>


    /**
     * Retrieves upload URLs for a user's profile picture.
     *
     * This method generates and provides the necessary URLs and metadata required for uploading
     * a profile picture. It includes the upload endpoint, the publicly accessible URL of the
     * picture after upload, and any additional request headers needed.
     *
     * @param mimeType The MIME type of the profile picture being uploaded (e.g., "image/jpeg", "image/png").
     * @return A `Result` containing either a `ProfilePictureUploadUrls` on success or a `DataError.Remote` on failure.
     */
    suspend fun getProfilePictureUploadUrl(
        mimeType: String
    ): Result<ProfilePictureUploadUrls, DataError.Remote>

    /**
     * Uploads a profile picture to the specified upload URL.
     *
     * This method uploads a byte array representing the profile picture to the provided URL.
     * Custom headers can be included as part of the request to meet specific API requirements.
     *
     * @param uploadUrl The URL to which the profile picture will be uploaded.
     * @param imageBytes The byte array containing the image data for the profile picture.
     * @param headers A map containing the headers to be included in the request.
     * @return An `EmptyResult` indicating success or failure, with potential failure caused by a `DataError.Remote`.
     */
    suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>
    ): EmptyResult<DataError.Remote>

    /**
     * Confirms the completion of a profile picture upload process.
     *
     * This method finalizes the profile picture upload by validating the public URL of the uploaded picture.
     *
     * @param publicUrl The public URL of the uploaded profile picture.
     * @return An `EmptyResult` indicating either success or a `DataError.Remote` in case of failure.
     */
    suspend fun confirmProfilePictureUpload(
        publicUrl: String
    ): EmptyResult<DataError.Remote>
}