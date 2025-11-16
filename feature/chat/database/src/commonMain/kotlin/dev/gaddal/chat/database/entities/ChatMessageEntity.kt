package dev.gaddal.chat.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Represents a message within a chat, containing details about the sender, content, and its metadata.
 *
 * This entity defines the schema for storing messages in the database, including associations
 * with a specific chat through the `chatId` foreign key. The message is uniquely identified
 * by its `messageId`.
 *
 * Each message contains the following information:
 * - `messageId`: A unique identifier for the message.
 * - `chatId`: The identifier of the chat to which this message belongs.
 * - `senderId`: The identifier of the participant who sent the message.
 * - `content`: The text or content of the message.
 * - `timestamp`: The time at which the message was sent, represented in milliseconds since the epoch.
 * - `deliveryStatus`: The current delivery status of the message (e.g., sent, delivered, read).
 * - `deliveryStatusTimestamp`: The time when the delivery status was last updated, defaulting to the `timestamp`.
 *
 * This entity is associated with:
 * - `ChatEntity`: Links a message to its parent chat using the `chatId` foreign key.
 * - `ChatParticipantEntity`: Links the sender of this message using the `senderId` field.
 *
 * The `onDelete = ForeignKey.CASCADE` in the foreign key definition ensures that
 * when a `ChatEntity` is deleted, all its associated `ChatMessageEntity` instances are also removed automatically.
 */
@Entity(
    foreignKeys = [
        ForeignKey(
            entity = ChatEntity::class,
            parentColumns = ["chatId"],
            childColumns = ["chatId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("chatId"),
        Index("timestamp"),
    ]
)
data class ChatMessageEntity(
    @PrimaryKey
    val messageId: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val deliveryStatus: String,
    val deliveryStatusTimestamp: Long = timestamp
)