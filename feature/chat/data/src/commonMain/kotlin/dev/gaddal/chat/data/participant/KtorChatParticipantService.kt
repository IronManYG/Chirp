package dev.gaddal.chat.data.participant

import dev.gaddal.chat.data.dto.ChatParticipantDto
import dev.gaddal.chat.data.mappers.toDomain
import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.chat.domain.participant.ChatParticipantService
import dev.gaddal.core.data.networking.get
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.map
import io.ktor.client.HttpClient

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
}