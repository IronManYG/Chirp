package dev.gaddal.chat.domain.message

import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.chat.domain.models.ChatMessageDeliveryStatus
import dev.gaddal.chat.domain.models.MessageWithSender
import dev.gaddal.chat.domain.models.OutgoingNewMessage
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Defines a repository interface for managing chat messages, particularly for handling their delivery statuses.
 *
 * This interface is typically used to abstract the data operations related to chat messages,
 * allowing for an implementation-independent approach to manage these operations in a system.
 */
interface MessageRepository {
    /**
     * Updates the delivery status of a chat message specified by the messageId.
     *
     * This method is responsible for changing the delivery status of a message
     * in the underlying data source, facilitating status tracking for messages
     * sent or received in a conversation.
     *
     * @param messageId The unique identifier of the message whose delivery status is to be updated.
     * @param status The new delivery status to be assigned to the message. It should be one of
     * the values defined in the `ChatMessageDeliveryStatus` enum.
     * @return An `EmptyResult` indicating the success or failure of the operation. On failure,
     * it contains an instance of `DataError.Local` to represent the specific local error encountered.
     */
    suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local>

    /**
     * Fetches messages associated with a specific chat, optionally filtering messages
     * created before a given timestamp.
     *
     * Pagination deletion-sync compromise:
     * - When before == null (i.e., fetching the latest "first page"), the implementation
     *   should enable a deletion reconciliation pass in the DAO
     *   (e.g., call upsertMessagesAndSyncIfNecessary with shouldSync = true and the same page size
     *   as used by the network request).
     * - For older pages (before != null), we do NOT attempt deletion detection client-side due to cost.
     *   Robust support here would require server-provided deletion info (IDs/tombstones).
     *
     * @param chatId The unique identifier of the chat whose messages are to be fetched.
     * @param before An optional timestamp to filter messages created before this time.
     *               If null, messages are fetched without any time-based filtering and
     *               the first-page deletion sync should be considered.
     * @return A `Result` containing a list of `ChatMessage` on success, or a `DataError`
     *         on failure.
     *
     * TODO(server): Add explicit deletion propagation (e.g., tombstones or a "deletedIds" field)
     *               so we can reconcile deletions across all pages.
     * TODO(client): Ensure the DAO pageSize used for sync matches the network page size for before == null,
     *               otherwise the deletion window may not align.
     */
    suspend fun fetchMessages(
        chatId: String,
        before: String? = null
    ): Result<List<ChatMessage>, DataError>

    /**
     * Sends a new message in a chat.
     *
     * This method is responsible for handling the process of sending a new message
     * to a specified chat. It performs the necessary operations to deliver the message,
     * including interacting with the underlying data source and notifying the recipient(s).
     *
     * @param message The new outgoing message to be sent. Contains details such as the chat ID,
     *                message ID, and content of the message.
     * @return An `EmptyResult` indicating the success or failure of the operation. If the operation
     *         fails, a `DataError` provides details about the specific error encountered.
     */
    suspend fun sendMessage(message: OutgoingNewMessage): EmptyResult<DataError>

    /**
     * Attempts to retry sending a message identified by its unique message ID.
     *
     * This method is utilized when a previously sent message has encountered a failure
     * and needs to be re-delivered. It performs the necessary actions to attempt resending
     * the message, handling any required updates in the underlying data source or network
     * operations.
     *
     * @param messageId The unique identifier of the message to be retried.
     * @return An `EmptyResult` indicating the success or failure of the retry operation. On failure,
     * it contains an instance of `DataError` providing details about the specific error encountered.
     */
    suspend fun retryMessage(messageId: String): EmptyResult<DataError>

    /**
     * Deletes a message identified by its unique message ID.
     *
     * This method is responsible for removing a specific message from the underlying data source.
     * It facilitates the deletion of messages based on their unique identifier to support
     * message lifecycle management within a chat or messaging application.
     *
     * @param messageId The unique identifier of the message to be deleted.
     * @return An `EmptyResult` indicating the success or failure of the operation.
     *         On failure, it contains an instance of `DataError.Remote` providing details
     *         about the specific remote error encountered.
     */
    suspend fun deleteMessage(messageId: String): EmptyResult<DataError.Remote>

    /**
     * Retrieves a flow of messages for a specific chat identified by its unique identifier.
     *
     * This method continuously provides a stream of updates containing the list of messages
     * and their associated senders for a given chat. It is particularly useful for observing
     * real-time updates in a chat conversation, such as new message arrivals or updates to message states.
     *
     * @param chatId The unique identifier of the chat for which messages are to be retrieved.
     * @return A flow emitting lists of messages, each paired with their respective sender details.
     */
    fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>>
}