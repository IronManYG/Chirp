package dev.gaddal.chat.domain.message

import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result

/**
 * A service interface for handling chat messages.
 *
 * This interface provides operations for retrieving messages from a chat,
 * as well as deleting messages from the system.
 *
 */
interface ChatMessageService {
    /**
     * Fetches a list of chat messages for the specified chat.
     * The messages can optionally be limited to those created before a given timestamp.
     *
     * @param chatId The unique identifier of the chat whose messages are being fetched.
     * @param before Optional timestamp used to fetch messages created before this value. If null, fetches the latest messages.
     * @return A `Result` containing either a list of `ChatMessage` objects on success, or a `DataError.Remote` object on failure.
     */
    suspend fun fetchMessages(
        chatId: String,
        before: String? = null
    ): Result<List<ChatMessage>, DataError.Remote>

    /**
     * Deletes a chat message with the specified message ID.
     *
     * This function removes the message associated with the given ID
     * from the system. If the operation fails, a remote data error is returned.
     *
     * @param messageId The unique identifier of the message to be deleted.
     * @return An `EmptyResult` indicating the success or failure of the operation.
     *         On success, no additional data is returned. On failure, a `DataError.Remote`
     *         object provides details about the error.
     */
    suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote>
}