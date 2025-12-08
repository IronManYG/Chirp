package dev.gaddal.chat.domain.participant

import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result

/**
 * A repository interface for managing and retrieving chat participants.
 *
 * This interface defines the contract for interacting with chat participant data,
 * allowing for operations such as fetching the current user participating in a chat.
 */
interface ChatParticipantRepository {
    /**
     * Fetches the local participant associated with the current chat session.
     *
     * This method performs a suspended operation to retrieve details of the local participant,
     * including associated user information such as username, user ID, and profile picture (if any).
     * The result of the operation encapsulates either the successful participant data or an error
     * if the fetch operation fails.
     *
     * @return A [Result] containing a [ChatParticipant] in case of success or a [DataError] in case of failure.
     */
    suspend fun fetchLocalParticipant(): Result<ChatParticipant, DataError>

    /**
     * Uploads a profile picture for the currently authenticated user.
     *
     * This method allows uploading a new profile picture to the server by providing
     * the image data in the form of a byte array and specifying the image's MIME type.
     * It performs a suspended operation and returns a result indicating the success or failure
     * of the upload process.
     *
     * @param imageBytes The raw byte array representing the image to be uploaded.
     * @param mimeType The MIME type of the image (e.g., "image/png", "image/jpeg").
     * @return An [EmptyResult] indicating the outcome of the upload operation. In case of failure,
     *         it returns a [DataError.Remote] detailing the error.
     */
    suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyResult<DataError.Remote>

    /**
     * Deletes the profile picture of the currently authenticated user.
     *
     * This method performs a suspended operation to remove the existing profile picture
     * from the server. It ensures that the profile picture is no longer associated with
     * the user's account. The result of the operation indicates whether the deletion
     * was successful or if it encountered a remote error.
     *
     * @return An [EmptyResult] indicating the outcome of the delete operation. In case of failure,
     *         it returns a [DataError.Remote] detailing the error.
     */
    suspend fun deleteProfilePicture(): EmptyResult<DataError.Remote>
}