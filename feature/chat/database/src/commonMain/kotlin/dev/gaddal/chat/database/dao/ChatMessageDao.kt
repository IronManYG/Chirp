package dev.gaddal.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.gaddal.chat.database.entities.ChatMessageEntity
import dev.gaddal.chat.database.entities.MessageWithSender
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

/**
 * Data Access Object (DAO) for managing chat messages in the database.
 * Provides methods for inserting, updating, querying, and deleting chat messages.
 */
@Dao
interface ChatMessageDao {

    /**
     * Inserts a new message into the database or updates an existing one if it already exists,
     * based on its primary key.
     *
     * @param message The message entity to insert or update in the database.
     */
    @Upsert
    suspend fun upsertMessage(message: ChatMessageEntity)

    /**
     * Inserts or updates a list of chat messages in the database.
     *
     * This function ensures that if a message with the same primary key already exists in the database,
     * it will be updated. If no such message exists, a new entry will be inserted.
     *
     * @param messages A list of `ChatMessageEntity` objects representing the messages to be upserted.
     */
    @Upsert
    suspend fun upsertMessages(messages: List<ChatMessageEntity>)

    /**
     * Deletes a single message from the database based on its unique message ID.
     *
     * @param messageId The unique identifier of the message to be deleted.
     */
    @Query("DELETE FROM chatmessageentity WHERE messageId = :messageId")
    suspend fun deleteMessageById(messageId: String)

    /**
     * Deletes messages from the database that match the provided list of message IDs.
     *
     * @param messageIds A list of message IDs identifying the messages to be deleted.
     */
    @Query("DELETE FROM chatmessageentity WHERE messageId IN (:messageIds)")
    suspend fun deleteMessagesById(messageIds: List<String>)

    /**
     * Retrieves a flow of messages associated with a specific chat ID, ordered by timestamp in descending order.
     *
     * @param chatId The unique identifier of the chat whose messages are to be retrieved.
     * @return A Flow emitting a list of MessageWithSender objects representing the messages in the chat.
     */
    @Query("SELECT * FROM chatmessageentity WHERE chatId = :chatId ORDER BY timestamp DESC")
    fun getMessagesByChatId(chatId: String): Flow<List<MessageWithSender>>

    @Query(
        """
        SELECT *
        FROM chatmessageentity
        WHERE chatId = :chatId
        ORDER BY timestamp DESC
        LIMIT :limit
    """
    )
    fun getMessagesByChatIdLimited(chatId: String, limit: Int): Flow<List<ChatMessageEntity>>

    /**
     * Retrieves a single chat message by its unique message ID.
     *
     * This function queries the database to fetch the `ChatMessageEntity`
     * corresponding to the provided `messageId`. If no message is found
     * with the given ID, the function will return `null`.
     *
     * @param messageId The unique identifier of the message to be retrieved.
     * @return The `ChatMessageEntity` object matching the provided `messageId`, or `null` if no such message exists.
     */
    @Query("SELECT * FROM chatmessageentity WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): ChatMessageEntity?

    /**
     * Updates the delivery status and the associated timestamp of a message in the database.
     *
     * @param messageId The unique identifier of the message whose delivery status is to be updated.
     * @param status The new delivery status to be applied to the message.
     * @param timestamp The timestamp indicating when the delivery status was updated.
     */
    @Query(
        """
        UPDATE chatmessageentity
        SET deliveryStatus = :status, deliveryStatusTimestamp = :timestamp
        WHERE messageId = :messageId
    """
    )
    suspend fun updateDeliveryStatus(messageId: String, status: String, timestamp: Long)

    /**
     * Inserts or updates a list of server messages for a given chat and, optionally, synchronizes the
     * local DB by deleting messages that are missing on the server (within the first page window).
     *
     * Background/compromise:
     * - Unlike chats, messages are paginated and can be very large on the server.
     * - The client cannot efficiently detect deletions for the entire history without server support.
     * - Therefore, we only attempt deletion-sync for the "first page" (latest messages) and only if the
     *   caller explicitly enables it via shouldSync = true (e.g., when before == null on the fetch call).
     *
     * Behavior:
     * 1. Read the first page of local messages (pageSize).
     * 2. Upsert the provided server messages.
     * 3. If shouldSync is true, delete local messages that:
     *    - are not present in the server's first-page IDs AND
     *    - are already SENT locally (to avoid deleting pending/failed drafts).
     *
     * Limitations:
     * - Deletions older than the first page are not detected here. Proper server-side signals
     *   (e.g., tombstones, soft-delete flags, change streams, or explicit "deleted IDs" in responses)
     *   would be needed to robustly cover the full history.
     *
     * @param chatId The unique identifier of the chat for which the operation is performed.
     * @param serverMessages A list of `ChatMessageEntity` from the server to upsert.
     * @param pageSize The page size used for the "first page" window (should match the network page size).
     * @param shouldSync Set to true only when fetching the latest page (before == null) so that
     *                   we can safely reconcile deletions within that window.
     *
     * TODO(server): Provide a mechanism to convey deleted message IDs (or tombstones) so we can
     *               reconcile deletions beyond the first page without large re-reads.
     * TODO(client): Ensure pageSize here matches the page size used by the network layer to keep
     *               the deletion window consistent with the first-page response.
     */

    @Transaction
    suspend fun upsertMessagesAndSyncIfNecessary(
        chatId: String,
        serverMessages: List<ChatMessageEntity>,
        pageSize: Int,
        shouldSync: Boolean = false
    ) {
        val localMessages = getMessagesByChatIdLimited(
            chatId = chatId,
            limit = pageSize
        ).first()

        upsertMessages(serverMessages)

        if (!shouldSync) {
            return
        }

        val serverIds = serverMessages.map { it.messageId }.toSet()

        val messagesToDelete = localMessages.filter { localMessage ->
            val missingOnServer = localMessage.messageId !in serverIds
            val isSent = localMessage.deliveryStatus == "SENT"

            missingOnServer && isSent
        }

        val messageIds = messagesToDelete.map { it.messageId }
        deleteMessagesById(messageIds)
    }
}