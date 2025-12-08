package dev.gaddal.chat.data.participant

import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.chat.domain.participant.ChatParticipantRepository
import dev.gaddal.chat.domain.participant.ChatParticipantService
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.onSuccess
import kotlinx.coroutines.flow.first

/**
 * Implementation of the `ChatParticipantRepository` designed for offline-first operations.
 *
 * This repository coordinates fetching and storing chat participant data, with a focus on
 * utilizing local storage when possible and falling back to remote services as needed.
 * It integrates with `SessionStorage` for updating and observing authentication information
 * and `ChatParticipantService` for retrieving chat participant details.
 *
 * @param sessionStorage A `SessionStorage` instance used for managing session-related data.
 * @param chatParticipantService A `ChatParticipantService` instance for fetching participant data.
 */
class OfflineFirstChatParticipantRepository(
    private val sessionStorage: SessionStorage,
    private val chatParticipantService: ChatParticipantService
) : ChatParticipantRepository {

    /**
     * Fetches the local chat participant data and updates the session storage with the participant's details.
     *
     * The method retrieves the local participant's information, which includes the participant's user ID,
     * username, and profile picture URL. Once this data is fetched successfully, it updates the
     * session storage with the corresponding authentication information, modifying the stored user details
     * to match those of the fetched participant.
     *
     * @return A [Result] containing either the [ChatParticipant] instance on success or a [DataError] on failure.
     */
    override suspend fun fetchLocalParticipant(): Result<ChatParticipant, DataError> {
        return chatParticipantService
            .getLocalParticipant()
            .onSuccess { participant ->
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.set(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            id = participant.userId,
                            username = participant.username,
                            profilePictureUrl = participant.profilePictureUrl
                        )
                    )
                )
            }
    }

    /**
     * Uploads a user's profile picture to the server and updates the session storage with the new picture URL.
     *
     * This method handles the entire process of uploading a profile picture in multiple steps:
     * 1. Retrieves the upload URL and associated headers from the server based on the provided MIME type.
     * 2. Uploads the image data to the obtained upload URL.
     * 3. Confirms the upload and updates the session storage with the new profile picture's public URL.
     *
     * @param imageBytes The byte array representing the profile picture data to be uploaded.
     * @param mimeType The MIME type of the image being uploaded (e.g., "image/png" or "image/jpeg").
     * @return An [EmptyResult] indicating success or a [DataError.Remote] in case of failure during any step.
     */
    override suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyResult<DataError.Remote> {
        val result = chatParticipantService.getProfilePictureUploadUrl(mimeType)

        if (result is Result.Failure) {
            return result
        }

        val uploadUrls = (result as Result.Success).data
        val uploadResult = chatParticipantService.uploadProfilePicture(
            uploadUrl = uploadUrls.uploadUrl,
            imageBytes = imageBytes,
            headers = uploadUrls.headers
        )

        if (uploadResult is Result.Failure) {
            return uploadResult
        }

        return chatParticipantService
            .confirmProfilePictureUpload(uploadUrls.publicUrl)
            .onSuccess {
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.set(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            profilePictureUrl = uploadUrls.publicUrl
                        )
                    )
                )
            }
    }

    /**
     * Deletes the user's profile picture from the server and updates the session storage.
     *
     * This method removes the profile picture URL from the user's authentication information
     * stored in the session storage, ensuring the user's profile reflects the removal of the picture.
     *
     * @return An [EmptyResult] indicating success, or a [DataError.Remote] in case of failure during
     * the deletion process.
     */
    override suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote> {
        return chatParticipantService
            .deleteProfilePicture()
            .onSuccess {
                val authInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.set(
                    authInfo?.copy(
                        user = authInfo.user.copy(
                            profilePictureUrl = null
                        )
                    )
                )
            }
    }
}