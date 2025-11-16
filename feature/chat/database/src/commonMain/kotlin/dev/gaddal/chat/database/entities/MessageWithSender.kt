package dev.gaddal.chat.database.entities

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Represents a data relationship between a chat message and its sender.
 *
 * This data class is a composite object that combines details about a chat message
 * and the sender of the message. It establishes a one-to-one relationship between:
 * - `ChatMessageEntity`: Represents the details of the message, such as its content,
 *   timestamp, and delivery status.
 * - `ChatParticipantEntity`: Represents the sender's details, such as their unique user ID,
 *   username, and optional profile picture URL.
 *
 * The `MessageWithSender` class is used to retrieve enriched information about a
 * message along with the corresponding sender details. This is particularly useful
 * for displaying conversations in user interfaces where both message content and
 * sender information are needed.
 *
 * Relationships:
 * - The `@Relation` annotation establishes an association between the `senderId` field in
 *   `ChatMessageEntity` and the `userId` field in `ChatParticipantEntity`.
 * - The `@Embedded` annotation includes the message details as part of the composite object.
 */
data class MessageWithSender(
    @Embedded
    val message: ChatMessageEntity,
    @Relation(
        parentColumn = "senderId",
        entityColumn = "userId"
    )
    val sender: ChatParticipantEntity
)