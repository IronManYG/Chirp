package dev.gaddal.chat.domain.chat

import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result

/**
 * Service interface for managing chat participants.
 *
 * Provides functionality for searching and retrieving details about participants in a chat.
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
}