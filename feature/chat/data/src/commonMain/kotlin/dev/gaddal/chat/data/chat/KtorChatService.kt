package dev.gaddal.chat.data.chat

import dev.gaddal.chat.data.dto.ChatDto
import dev.gaddal.chat.data.dto.request.CreateChatRequest
import dev.gaddal.chat.data.mappers.toDomain
import dev.gaddal.chat.domain.chat.ChatService
import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.core.data.networking.get
import dev.gaddal.core.data.networking.post
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.map
import io.ktor.client.HttpClient

/**
 * Implementation of the `ChatService` interface that provides chat-related functionality using a Ktor-based HTTP client.
 *
 * This service allows interaction with chat-related endpoints, such as creating and retrieving chats.
 * It leverages the `HttpClient` from Ktor for communication with the remote server.
 *
 * @constructor Initializes the `KtorChatService` with the provided HTTP client.
 * @param httpClient The Ktor HTTP client used to perform network operations.
 */
class KtorChatService(
    private val httpClient: HttpClient
) : ChatService {

    /**
     * Creates a new chat with the specified list of other user IDs.
     *
     * @param otherUserIds A list of IDs representing the other users to include in the chat.
     * This list should not include the ID of the calling user as it will be handled automatically.
     * @return A [Result] containing the successfully created [Chat] if the operation is successful,
     * or a [DataError.Remote] if an error occurs during the creation process.
     */
    override suspend fun createChat(otherUserIds: List<String>): Result<Chat, DataError.Remote> {
        return httpClient.post<CreateChatRequest, ChatDto>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds
            )
        ).map { it.toDomain() }
    }

    /**
     * Retrieves a list of chats associated with the calling user.
     *
     * This method performs an HTTP GET request to fetch a collection of chats, converts
     * the response into domain models, and wraps the result in a [Result] object. The
     * result can either contain the list of [Chat] objects or a [DataError.Remote] if
     * an error occurs during the fetch operation.
     *
     * @return A [Result] containing a list of [Chat] objects on success, or a [DataError.Remote] on failure.
     */
    override suspend fun getChats(): Result<List<Chat>, DataError.Remote> {
        return httpClient.get<List<ChatDto>>(
            route = "/chat"
        ).map { chatDtos ->
            chatDtos.map { it.toDomain() }
        }
    }
}