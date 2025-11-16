package dev.gaddal.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import dev.gaddal.chat.database.entities.ChatParticipantCrossRef
import dev.gaddal.chat.database.entities.ChatParticipantEntity

/**
 * DAO interface for managing the `ChatParticipantCrossRef` table. This table serves as a
 * cross-reference between chats and their participants, supporting many-to-many relationships
 * and maintaining additional metadata such as participant activity status.
 *
 * The interface includes methods to perform various operations, such as inserting or updating
 * records, querying active and inactive participants, and syncing participant data with an external source.
 * It plays a critical role in facilitating participant management in the chat system.
 */
@Dao
interface ChatParticipantsCrossRefDao {

    /**
     * Inserts or updates a list of `ChatParticipantCrossRef` entries in the database.
     *
     * This method ensures that the provided cross-reference entities, representing the relationships
     * between chats and their participants, are added or updated in the database. If a matching entry
     * already exists, it will be updated; otherwise, a new entry will be inserted.
     *
     * @param crossRefs A list of `ChatParticipantCrossRef` instances to be upserted in the database.
     */
    @Upsert
    suspend fun upsertCrossRefs(crossRefs: List<ChatParticipantCrossRef>)

    /**
     * Retrieves a list of user IDs who are active participants in a specific chat.
     *
     * @param chatId The unique identifier of the chat whose active participant IDs are to be fetched.
     * @return A list of user IDs representing the active participants in the specified chat.
     */
    @Query("SELECT userId FROM chatparticipantcrossref WHERE chatId = :chatId")
    suspend fun getActiveParticipantIdsByChat(chatId: String): List<String>

    /**
     * Retrieves all participant IDs associated with a specific chat.
     *
     * @param chatId The unique identifier of the chat for which participant IDs are being retrieved.
     * @return A list of participant IDs (as strings) associated with the given chat.
     */
    @Query("SELECT userId FROM chatparticipantcrossref")
    suspend fun getAllParticipantIdsByChat(chatId: String): List<String>

    /**
     * Marks the specified participants as inactive in a given chat.
     *
     * @param chatId the ID of the chat where the participants should be marked as inactive
     * @param userIds the list of user IDs to be marked as inactive
     */
    @Query(
        """
        UPDATE chatparticipantcrossref
        SET isActive = 0
        WHERE chatId = :chatId AND userId IN (:userIds)
    """
    )
    suspend fun markParticipantsAsInactive(chatId: String, userIds: List<String>)

    /**
     * Reactivates the given participants in a specific chat by setting their `isActive` status to 1.
     *
     * @param chatId The unique identifier of the chat where the participants belong.
     * @param userIds A list of unique identifiers for the users to be reactivated in the specified chat.
     */
    @Query(
        """
        UPDATE chatparticipantcrossref
        SET isActive = 1
        WHERE chatId = :chatId AND userId IN (:userIds)
    """
    )
    suspend fun reactivateParticipants(chatId: String, userIds: List<String>)

    /**
     * Synchronizes chat participants in the local database with the provided list of participants.
     *
     * This function updates the `chatparticipantcrossref` table to reflect the state of participants
     * received from a server or external source. It performs the following tasks:
     *
     * 1. Determines participants to reactivate (currently inactive but present in the provided list)
     *    and reactivates them.
     * 2. Marks participants as inactive if they are active in the database but not present in the
     *    provided list.
     * 3. Identifies completely new participants, creates cross-references for them, and marks them
     *    as active.
     *
     * The function ensures that the local database accurately represents the current chat participant
     * state based on the provided input.
     *
     * @param chatId The unique identifier of the chat whose participants are being synchronized.
     * @param participants A list of `ChatParticipantEntity` objects representing the current participants
     *                     of the chat.
     */
    @Transaction
    suspend fun syncChatParticipants(
        chatId: String,
        participants: List<ChatParticipantEntity>
    ) {
        if (participants.isEmpty()) {
            return
        }

        val serverParticipantIds = participants.map { it.userId }.toSet()
        val allLocalParticipantIds = getAllParticipantIdsByChat(chatId).toSet()
        val activeLocalParticipantIds = getActiveParticipantIdsByChat(chatId).toSet()
        val inactiveLocalParticipantIds = allLocalParticipantIds - activeLocalParticipantIds

        val participantsToReactivate = serverParticipantIds.intersect(inactiveLocalParticipantIds)
        val participantsToDeactivate = activeLocalParticipantIds - serverParticipantIds

        reactivateParticipants(chatId, participantsToReactivate.toList())
        markParticipantsAsInactive(chatId, participantsToDeactivate.toList())

        val completelyNewParticipantIds = serverParticipantIds - allLocalParticipantIds
        val newCrossRefs = completelyNewParticipantIds.map { userId ->
            ChatParticipantCrossRef(
                chatId = chatId,
                userId = userId,
                isActive = true
            )
        }
        upsertCrossRefs(newCrossRefs)
    }
}