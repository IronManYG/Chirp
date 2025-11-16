package dev.gaddal.chat.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a chat in the database, containing details about its unique identifier,
 * the last message, and the time of the last activity.
 *
 * This entity defines the schema for storing chat-related data, such as tracking the chat's
 * latest activity and the content of the last message. The `chatId` serves as the primary key
 * and uniquely identifies the chat.
 *
 * Key features of this entity include:
 * - `chatId`: A unique identifier for the chat.
 * - `lastMessage`: The content of the last message in the chat, if any.
 * - `lastActivityAt`: A timestamp indicating the last recorded activity in the chat,
 *   represented in milliseconds since the epoch.
 *
 * Relationships:
 * - Associated with `ChatParticipantEntity` through `ChatParticipantCrossRef` to establish
 *   the participants of the chat.
 * - Associated with `ChatMessageEntity` to hold the messages exchanged in the chat.
 * - Used in composite data classes like `ChatWithParticipants` and `ChatInfoEntity`
 *   to support retrieval of chats with participants and messages.
 *
 * Deletion Behavior:
 * When a chat is deleted, its related data, such as participants and messages, is also
 * automatically removed due to cascading foreign key constraints in related entities.
 */
@Entity
data class ChatEntity(
    @PrimaryKey
    val chatId: String,
    val lastMessage: String?,
    val lastActivityAt: Long
)