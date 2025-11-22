package dev.gaddal.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.gaddal.chat.database.entities.ChatParticipantEntity

/**
 * Data Access Object (DAO) for managing chat participants in the local database.
 *
 * Provides methods to insert or update participants, as well as retrieve all participants
 * stored in the database. The `ChatParticipantDao` interacts with the `ChatParticipantEntity`
 * to perform CRUD operations necessary for managing participants in a chat system.
 */
@Dao
interface ChatParticipantDao {

    /**
     * Inserts a new chat participant into the database or updates it if it already exists.
     *
     * This method ensures that the `ChatParticipantEntity` provided is either inserted as a new
     * entity or updated if an existing entity with the same primary key (`userId`) already exists.
     * It is typically used to synchronize participant information in a chat-related context.
     *
     * @param participant The chat participant entity to be inserted or updated. It contains details
     * such as the participant's unique identifier (`userId`), their username, and an optional
     * profile picture URL.
     */
    @Upsert
    suspend fun upsertParticipant(participant: ChatParticipantEntity)

    /**
     * Inserts or updates a list of chat participants in the database.
     * If a participant already exists (based on its primary key), it will be updated.
     * Otherwise, a new participant entry will be inserted.
     *
     * @param participants The list of `ChatParticipantEntity` objects to be upserted.
     */
    @Upsert
    suspend fun upsertParticipants(participants: List<ChatParticipantEntity>)


    /**
     * Updates the profile picture URL of a chat participant in the database.
     *
     * This method updates the `profilePictureUrl` field for a participant identified by their unique `userId`.
     * If the `newUrl` is null, it clears the existing profile picture URL.
     *
     * @param userId The unique identifier of the user whose profile picture URL is being updated.
     * @param newUrl The new URL to set as the profile picture, or null to clear the profile picture URL.
     */
    @Query(
        """
        UPDATE chatparticipantentity
        SET profilePictureUrl = :newUrl
        WHERE userId = :userId
    """
    )
    suspend fun updateProfilePictureUrl(userId: String, newUrl: String?)

    /**
     * Retrieves all participants from the chat participant database entity.
     *
     * @return A list of all participants stored as instances of ChatParticipantEntity.
     */
    @Query("SELECT * FROM chatparticipantentity")
    suspend fun getAllParticipants(): List<ChatParticipantEntity>
}