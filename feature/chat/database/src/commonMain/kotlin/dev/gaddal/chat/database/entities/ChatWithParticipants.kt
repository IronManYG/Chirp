package dev.gaddal.chat.database.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

/**
 * Represents a data structure that combines a chat with its associated participants.
 *
 * This class is a composite entity used to retrieve both chat details and the list
 * of participants in a single query. It utilizes Room's `@Embedded` and `@Relation`
 * annotations to define its structure and relationships.
 *
 * Attributes:
 * - `chat`: The chat entity containing details about the chat, such as its identifier,
 *   last message, and the timestamp of the last activity.
 * - `participants`: A list of chat participant entities associated with the chat. The
 *   relationship is defined through a many-to-many association via the `ChatParticipantCrossRef`
 *   entity, which links chats to their participants.
 *
 * Room Annotations:
 * - `@Embedded`: Used to embed the `ChatEntity` directly into this data structure.
 * - `@Relation`: Establishes the relationship between the `ChatEntity` and its participants,
 *   specifying the columns (`chatId` and `userId`) and the junction entity (`ChatParticipantCrossRef`)
 *   for the many-to-many association.
 *
 * Purpose:
 * This class is useful for retrieving comprehensive chat data along with all participants,
 * making it suitable for scenarios requiring both chat metadata and participant details.
 * Examples include chat listings, participant management, and other features requiring
 * combined data representation.
 */
data class ChatWithParticipants(
    @Embedded
    val chat: ChatEntity,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "userId",
        associateBy = Junction(ChatParticipantCrossRef::class)
    )
    val participants: List<ChatParticipantEntity>
)

/**
 * Represents a data class used to retrieve comprehensive information about a chat,
 * including the chat details, its participants, and messages with their senders.
 *
 * This class combines multiple entities and relationships to provide a unified view
 * of a chat's data. It incorporates:
 *
 * 1. Basic details about the chat, obtained from the `ChatEntity`.
 * 2. Information about the participants of the chat, retrieved through the many-to-many
 *    relationship between `ChatEntity` and `ChatParticipantEntity` using `ChatParticipantCrossRef`.
 * 3. Messages within the chat along with their corresponding sender information. This is
 *    modeled through a one-to-many relationship between `ChatEntity` and `ChatMessageEntity`,
 *    and an additional relationship to fetch sender details from `ChatParticipantEntity`.
 *
 * This class enables comprehensive and efficient retrieval of:
 * - Chat metadata, such as its ID, last message, and last activity timestamp.
 * - All participants involved in the chat.
 * - Messages within the chat, along with details about the senders.
 */
data class ChatInfoEntity(
    @Embedded
    val chat: ChatEntity,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "userId",
        associateBy = Junction(ChatParticipantCrossRef::class)
    )
    val participants: List<ChatParticipantEntity>,
    @Relation(
        parentColumn = "chatId",
        entityColumn = "chatId",
        entity = ChatMessageEntity::class
    )
    val messagesWithSenders: List<MessageWithSender>
)