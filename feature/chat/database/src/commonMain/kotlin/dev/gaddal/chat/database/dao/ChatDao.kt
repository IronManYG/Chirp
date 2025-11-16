package dev.gaddal.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.gaddal.chat.database.entities.ChatEntity
import dev.gaddal.chat.database.entities.ChatInfoEntity
import dev.gaddal.chat.database.entities.ChatParticipantCrossRef
import dev.gaddal.chat.database.entities.ChatParticipantEntity
import dev.gaddal.chat.database.entities.ChatWithParticipants
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) interface for managing chat-related operations in the database.
 *
 * This interface defines methods for inserting, updating, deleting, and querying chat entities and
 * their associated data. It utilizes Room Database annotations to execute SQL queries and manage
 * database transactions.
 *
 * Key functionalities:
 * - CRUD operations on `ChatEntity` objects.
 * - Complex queries to retrieve composite data, such as chats with participants and chat information.
 * - Transactional operations for batch deletions.
 * - Real-time updates of specific data through Flow-based methods.
 *
 * Relationships:
 * - The DAO works with entities like `ChatEntity`, `ChatWithParticipants`, `ChatParticipantEntity`,
 *   and `ChatInfoEntity` to manage data across related tables.
 * - Many-to-many relationships between chats and participants are handled by cross-reference entities.
 */
@Dao
interface ChatDao {

    /**
     * Inserts a new chat or updates an existing chat in the database.
     *
     * If a chat with the same `chatId` already exists, its details will be updated.
     * If no such chat exists, a new one will be added to the database.
     *
     * This method leverages the `@Upsert` annotation to perform the operation,
     * ensuring efficient handling of both insert and update scenarios based on the
     * primary key (`chatId`) of the `ChatEntity`.
     *
     * @param chat The chat entity containing details such as the `chatId`, last message,
     *             and last activity timestamp to be inserted or updated in the database.
     */
    @Upsert
    suspend fun upsertChat(chat: ChatEntity)

    /**
     * Inserts or updates a list of chat entities in the database. If a chat entity with the same
     * primary key already exists, it will be replaced; otherwise, a new record will be inserted.
     *
     * This operation ensures that the database remains synchronized with the latest state of
     * the provided chat entities, supporting use cases such as bulk chat synchronization or
     * updates.
     *
     * @param chats A list of chat entities to be inserted or updated in the database. Each
     *              entity represents a chat and contains details such as its unique identifier,
     *              the last message content, and the timestamp of the last activity.
     */
    @Upsert
    suspend fun upsertChats(chats: List<ChatEntity>)

    /**
     * Deletes a chat record from the database using the specified chat ID.
     *
     * This method removes the chat entity corresponding to the provided `chatId` parameter
     * from the `chatentity` table. It is used for managing chat data by enabling deletion of
     * individual chats while ensuring the integrity of other related data.
     *
     * @param chatId The unique identifier of the chat to be deleted from the database.
     */
    @Query("DELETE FROM chatentity WHERE chatId = :chatId")
    suspend fun deleteChatById(chatId: String)

    /**
     * Retrieves a flow of chats along with their associated participants, ordered by the last activity timestamp in descending order.
     *
     * This function allows observing a live list of chats and their participants using Kotlin's Flow API.
     * Each emitted list contains composite data that includes chat details and the corresponding participants.
     *
     * @return A Flow emitting lists of `ChatWithParticipants` objects, where each object combines chat details
     *         and its associated participants.
     */
    @Query("SELECT * FROM chatentity ORDER BY lastActivityAt DESC")
    fun getChatsWithParticipants(): Flow<List<ChatWithParticipants>>

    /**
     * Retrieves a chat along with its associated participants by the given chat ID.
     *
     * The method queries the database to fetch the chat details as well as the list of
     * participants for the specified chat ID. If the chat does not exist, it returns `null`.
     *
     * @param id The unique identifier of the chat to retrieve.
     * @return A `ChatWithParticipants` instance containing the chat details and its participants,
     * or `null` if no chat exists for the provided ID.
     */
    @Query("SELECT * FROM chatentity WHERE chatId = :id")
    suspend fun getChatById(id: String): ChatWithParticipants?

    /**
     * Deletes all existing chat entities from the `chatentity` table.
     *
     * This method removes all records from the `chatentity` table, effectively clearing
     * all chats stored in the database. It performs a bulk delete operation and does not
     * target specific rows; instead, the entire table content is erased.
     *
     * Use this method with caution as it will result in the irreversible loss of all
     * chat data, including related participant and message information depending on the
     * database schema constraints (such as cascading relationships).
     *
     * This operation is intended for scenarios where a complete reset or cleanup of chat
     * data is required.
     */
    @Query("DELETE FROM chatentity")
    suspend fun deleteAllChats()

    /**
     * Retrieves a list of all chat IDs from the database.
     *
     * This method queries the `chatentity` table to fetch the unique `chatId`
     * values for all the chats stored in the database. It provides an overview
     * of all chat identifiers without fetching additional chat details.
     *
     * @return A list of strings representing the IDs of all chats in the database.
     */
    @Query("SELECT chatId FROM chatentity")
    suspend fun getAllChatIds(): List<String>

    /**
     * Deletes multiple chats from the database based on their unique IDs.
     * This method performs the deletion operation in a transactional manner, iterating
     * through each provided chat ID and invoking the `deleteChatById` method for deletion.
     *
     * @param chatIds A list of unique identifiers for the chats to be deleted.
     */
    @Transaction
    suspend fun deleteChatsByIds(chatIds: List<String>) {
        chatIds.forEach { chatId ->
            deleteChatById(chatId)
        }
    }

    /**
     * Retrieves the total count of chats present in the database.
     *
     * This method queries the `ChatEntity` table to count the number of existing chat records.
     *
     * @return A [Flow] emitting the count of chats as an [Int].
     */
    @Query("SELECT COUNT(*) FROM chatentity")
    fun getChatCount(): Flow<Int>

    /**
     * Retrieves a flow of active participants in a specific chat identified by the chat ID.
     *
     * This method queries the database to fetch the list of participants who are marked as active
     * within the specified chat. The results are ordered by the username of the participants.
     * It establishes a many-to-many relationship between the chat and its participants through
     * the `ChatParticipantCrossRef` entity and filters participants based on their active status.
     *
     * @param chatId The unique identifier of the chat whose active participants are to be retrieved.
     * @return A `Flow` emitting lists of `ChatParticipantEntity` that represent the active participants
     *         in the given chat. Each list contains the most up-to-date participant data ordered by username.
     */
    @Query(
        """
        SELECT p.*
        FROM chatparticipantentity p
        JOIN chatparticipantcrossref cpcr ON p.userId = cpcr.userId
        WHERE cpcr.chatId = :chatId AND cpcr.isActive = true
        ORDER BY p.username
    """
    )
    fun getActiveParticipantsByChatId(chatId: String): Flow<List<ChatParticipantEntity>>

    /**
     * Retrieves information about a specific chat, including its details, participants,
     * and messages with their corresponding senders, based on the provided chat ID.
     *
     * This method uses a SQL query to fetch data from the `ChatEntity` table and its
     * associated relationships, returning a `Flow` to observe the data changes over time.
     *
     * @param chatId The unique identifier of the chat whose information is to be retrieved.
     * @return A Flow emitting a `ChatInfoEntity` object containing detailed chat information,
     *         or null if no chat is found with the given ID.
     */
    @Query("SELECT * FROM chatentity WHERE chatId = :chatId")
    fun getChatInfoById(chatId: String): Flow<ChatInfoEntity?>


    /**
     * Inserts or updates a chat entity, its participants, and their associated cross-references in the database.
     *
     * This method ensures that the chat and its related participants are stored or updated correctly,
     * maintaining their associations through cross-reference records. It updates the active status
     * of the cross-references and synchronizes the participants for a given chat.
     *
     * @param chat The chat entity to be inserted or updated.
     * @param participants A list of chat participants associated with the chat to be inserted or updated.
     * @param participantDao The DAO responsible for handling participant-related operations.
     * @param crossRefDao The DAO responsible for managing cross-references between chats and participants.
     */
    @Transaction
    suspend fun upsertChatWithParticipantsAndCrossRefs(
        chat: ChatEntity,
        participants: List<ChatParticipantEntity>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantsCrossRefDao
    ) {
        upsertChat(chat)
        participantDao.upsertParticipants(participants)

        val crossRefs = participants.map {
            ChatParticipantCrossRef(
                chatId = chat.chatId,
                userId = it.userId,
                isActive = true
            )
        }
        crossRefDao.upsertCrossRefs(crossRefs)
        crossRefDao.syncChatParticipants(chat.chatId, participants)
    }

    /**
     * Inserts or updates a list of chats along with their participants and cross-references in the database.
     *
     * This method performs a transactional operation to ensure consistency. It first upserts the provided
     * chats, then handles participants, and finally updates the cross-references connecting the chats and
     * their participants. The operation includes syncing the cross-references to reflect the latest changes.
     *
     * The method ensures that both chat data and its associated participants remain up-to-date and
     * synchronized in the database.
     *
     * @param chats A list of `ChatWithParticipants` objects, where each object contains a chat and its associated participants.
     * @param participantDao The DAO responsible for handling chat participant operations in the database.
     * @param crossRefDao The DAO responsible for handling cross-references between chats and participants in the database.
     */
    @Transaction
    suspend fun upsertChatsWithParticipantsAndCrossRefs(
        chats: List<ChatWithParticipants>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantsCrossRefDao
    ) {
        upsertChats(chats.map { it.chat })

        val allParticipants = chats.flatMap { it.participants }
        participantDao.upsertParticipants(allParticipants)

        val allCrossRefs = chats.flatMap { chatWithParticipants ->
            chatWithParticipants.participants.map { participant ->
                ChatParticipantCrossRef(
                    chatId = chatWithParticipants.chat.chatId,
                    userId = participant.userId,
                    isActive = true
                )
            }
        }
        crossRefDao.upsertCrossRefs(allCrossRefs)

        chats.forEach { chat ->
            crossRefDao.syncChatParticipants(
                chatId = chat.chat.chatId,
                participants = chat.participants
            )
        }
    }
}