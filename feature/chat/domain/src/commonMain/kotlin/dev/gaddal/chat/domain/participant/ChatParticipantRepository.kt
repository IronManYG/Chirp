package dev.gaddal.chat.domain.participant

import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.core.domain.util.DataError
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
}