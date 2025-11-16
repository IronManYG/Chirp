package dev.gaddal.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.gaddal.chat.database.entities.ChatMessageEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for managing chat messages in the database.
 * Provides methods for inserting, querying, and deleting chat messages.
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
    suspend fun deleteMessageById(messageIds: List<String>)

    /**
     * Retrieves a flow of messages associated with a specific chat ID, ordered by timestamp in descending order.
     *
     * @param chatId The unique identifier of the chat whose messages are to be retrieved.
     * @return A Flow emitting a list of ChatMessageEntity objects representing the messages in the chat.
     */
    @Query("SELECT * FROM chatmessageentity WHERE chatId = :chatId ORDER BY timestamp DESC")
    fun getMessagesByChatId(chatId: String): Flow<List<ChatMessageEntity>>

    /**
     * Retrieves a single message from the database based on the given message ID.
     *
     * @param messageId The unique identifier of the message to retrieve.
     * @return A [ChatMessageEntity] representing the message with the specified ID, or `null` if no such message exists.
     */
    @Query("SELECT * FROM chatmessageentity WHERE messageId = :messageId")
    suspend fun getMessageById(messageId: String): ChatMessageEntity?
}