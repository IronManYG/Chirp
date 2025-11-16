package dev.gaddal.chat.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

/**
 * Represents a cross-reference entity for establishing a many-to-many relationship
 * between `ChatEntity` and `ChatParticipantEntity`.
 *
 * This entity is used in the database schema to associate chats with their participants.
 * Each entry in this table links a chat (`chatId`) with a participant (`userId`),
 * and includes an `isActive` flag to indicate the participant's status in the chat.
 *
 * Key attributes:
 * - `chatId`: The unique identifier of the chat this participant belongs to.
 * - `userId`: The unique identifier of the participant.
 * - `isActive`: A boolean indicating whether the participant is currently active in the chat.
 *
 * Relationships:
 * - `ChatEntity`: Linked through the `chatId` field. When a chat is deleted, all
 *   associated entries in this entity are also deleted due to cascading behavior.
 * - `ChatParticipantEntity`: Linked through the `userId` field. When a participant is
 *   deleted, all associated entries in this entity are also deleted due to cascading behavior.
 *
 * This entity is critical in supporting the retrieval of chat-related data, including:
 * - Finding all participants of a specific chat, as used in composite data classes like `ChatWithParticipants`.
 * - Determining the active status of participants within a chat.
 */
@Entity(
    primaryKeys = ["chatId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["chatId"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ChatParticipantEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
    ]
)
data class ChatParticipantCrossRef(
    val chatId: String,
    val userId: String,
    val isActive: Boolean
)