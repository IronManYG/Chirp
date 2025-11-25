package dev.gaddal.chat.data.message

import dev.gaddal.chat.data.dto.ChatMessageDto
import dev.gaddal.chat.data.mappers.toDomain
import dev.gaddal.chat.domain.message.ChatMessageService
import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.core.data.networking.delete
import dev.gaddal.core.data.networking.get
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.map
import io.ktor.client.HttpClient

/**
 * A service implementation using Ktor to fetch chat messages.
 *
 * This class provides functionality to interact with a backend system
 * to retrieve chat messages for a given chat ID. It utilizes the Ktor
 * `HttpClient` for HTTP requests and adheres to the `ChatMessageService` interface.
 *
 * @constructor Initializes the service with the provided `HttpClient` instance.
 * @param httpClient The Ktor `HttpClient` used for making HTTP requests.
 */
class KtorChatMessageService(
    private val httpClient: HttpClient
) : ChatMessageService {

    /**
     * Fetches the list of chat messages for a given chat ID, optionally filtered to include
     * only messages sent before a specific message identifier.
     *
     * @param chatId The unique identifier of the chat for which messages are being fetched.
     * @param before The identifier of a message. If provided, fetches messages sent before this message.
     * @return A [Result] containing a list of [ChatMessage] on success or a [DataError.Remote] on failure.
     */
    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError.Remote> {
        return httpClient.get<List<ChatMessageDto>>(
            route = "/chat/$chatId/messages",
            queryParams = buildMap {
                this["pageSize"] = ChatMessageConstants.PAGE_SIZE
                if (before != null) {
                    this["before"] = before
                }
            }
        ).map { it.map { it.toDomain() } }
    }

    /**
     * Deletes a message with the specified identifier from the server.
     *
     * @param messageId The unique identifier of the message to delete.
     * @return An [EmptyResult] indicating success or a [DataError.Remote] in case of a failure.
     */
    override suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/messages/$messageId"
        )
    }
}