package dev.gaddal.chat.domain.participant

import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.core.domain.util.DataError
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
}