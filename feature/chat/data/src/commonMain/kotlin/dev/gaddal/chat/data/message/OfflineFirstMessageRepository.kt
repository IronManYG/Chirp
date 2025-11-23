package dev.gaddal.chat.data.message

import dev.gaddal.chat.data.mappers.toDomain
import dev.gaddal.chat.data.mappers.toEntity
import dev.gaddal.chat.database.ChirpChatDatabase
import dev.gaddal.chat.domain.message.ChatMessageService
import dev.gaddal.chat.domain.message.MessageRepository
import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.chat.domain.models.ChatMessageDeliveryStatus
import dev.gaddal.chat.domain.models.MessageWithSender
import dev.gaddal.core.data.database.safeDatabaseUpdate
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.time.Clock

/**
 * An implementation of the `MessageRepository` interface that adheres to the Offline-First
 * strategy for managing message-related operations. This repository prioritizes database
 * updates for chat messages, ensuring local persistence even without network connectivity.
 *
 * @param database The database instance of `ChirpChatDatabase` used for accessing and updating
 * chat-related data, such as delivery statuses.
 * @param chatMessageService The service responsible for fetching chat messages from remote sources.
 */
class OfflineFirstMessageRepository(
    private val database: ChirpChatDatabase,
    private val chatMessageService: ChatMessageService
) : MessageRepository {

    /**
     * Updates the delivery status of a chat message in the local database.
     *
     * This method modifies the status of a message identified by its unique ID,
     * setting it to the specified delivery status along with the current timestamp.
     *
     * @param messageId The unique identifier of the message whose delivery status is being updated.
     * @param status The new delivery status to be applied, represented by the `ChatMessageDeliveryStatus` enum.
     * @return An `EmptyResult` indicating success or failure of the operation.
     *         In case of failure, it returns a `DataError.Local`.
     */
    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: ChatMessageDeliveryStatus
    ): EmptyResult<DataError.Local> {
        return safeDatabaseUpdate {
            database.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                status = status.name,
                timestamp = Clock.System.now().toEpochMilliseconds()
            )
        }
    }

    /**
     * Fetches a list of chat messages for a specified chat and optional pagination cursor.
     *
     * This method interacts with a remote service to retrieve messages associated with the given
     * chat ID. If provided, the `before` parameter specifies the cursor for pagination to fetch
     * older messages. Upon successful retrieval, the messages are cached in the local database,
     * with optional synchronization for the most recent messages when `before` is not set.
     *
     * @param chatId The unique identifier of the chat for which messages are being fetched.
     * @param before An optional cursor indicating the point before which messages should be fetched.
     *               If null, the most recent messages are fetched.
     * @return A `Result` containing either a list of `ChatMessage` objects on success,
     *         or a `DataError` on failure.
     */
    override suspend fun fetchMessages(
        chatId: String,
        before: String?
    ): Result<List<ChatMessage>, DataError> {
        return chatMessageService
            .fetchMessages(chatId, before)
            .onSuccess { messages ->
                return safeDatabaseUpdate {
                    database.chatMessageDao.upsertMessagesAndSyncIfNecessary(
                        chatId = chatId,
                        serverMessages = messages.map { it.toEntity() },
                        pageSize = ChatMessageConstants.PAGE_SIZE,
                        shouldSync = before == null // Only sync for most recent page
                    )
                    messages
                }
            }
    }

    /**
     * Retrieves a flow of messages for a specific chat, mapping them to their domain representation.
     *
     * This method fetches all messages associated with the provided chat ID from the local database.
     * The messages are then transformed into a list of domain models, including the message details,
     * sender information, and their delivery status.
     *
     * @param chatId The unique identifier of the chat whose messages are to be retrieved.
     * @return A Flow emitting a list of MessageWithSender domain objects representing the messages and their associated sender information.
     */
    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> {
        return database
            .chatMessageDao
            .getMessagesByChatId(chatId)
            .map { messages ->
                messages.map { it.toDomain() }
            }
    }
}