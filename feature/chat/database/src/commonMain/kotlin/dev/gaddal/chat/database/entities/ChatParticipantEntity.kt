package dev.gaddal.chat.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a participant in a chat, identified by their unique user ID.
 *
 * This entity is used to store information about users who are part of a chat,
 * such as their identifier (`userId`), display name (`username`), and an optional
 * profile picture URL (`profilePictureUrl`).
 *
 * The `ChatParticipantEntity` is associated with multiple other database entities to
 * relate users to chats and messages in the database schema. Examples include:
 *
 * - `ChatParticipantCrossRef`: Defines the association between chat participants
 *   and chat entities, enabling many-to-many relationships.
 * - `ChatWithParticipants`: Defines a relationship to retrieve chat details along
 *   with the associated participants.
 * - `MessageWithSender`: Represents the sender details of a message linked through
 *   the `senderId` property.
 */
@Entity
data class ChatParticipantEntity(
    @PrimaryKey
    val userId: String,
    val username: String,
    val profilePictureUrl: String?
)