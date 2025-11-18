package dev.gaddal.chat.database.view

import androidx.room.DatabaseView

/**
 * Represents a database view that retrieves the latest message for each chat.
 *
 * The `LastMessageView` is a read-only representation of the most recent messages
 * across all chats in the database. It is constructed using an SQL `JOIN` operation
 * to identify the message with the maximum timestamp for each chat. This view is primarily
 * used to efficiently fetch the last message content along with key metadata such as
 * sender ID and the message's timestamp.
 *
 * Key attributes:
 * - `messageId`: The unique identifier of the last message in the chat.
 * - `chatId`: The identifier of the chat to which this message belongs.
 * - `senderId`: The ID of the participant who sent the message.
 * - `content`: The content of the last message, such as the text or media information.
 * - `timestamp`: The time at which the last message was sent, represented in milliseconds since the epoch.
 * - `deliveryStatus`: The current delivery status of the message (e.g., sent, delivered, read).
 *
 * Purpose:
 * This view reduces the complexity of retrieving the most recent message for discussions
 * or UI representations, such as chat lists where only the latest message is displayed
 * along with the chat information.
 *
 * Relationships:
 * - `chatId` links to the `ChatEntity` to fetch parent chat details.
 * - `senderId` links to `ChatParticipantEntity` to identify the sender of the message.
 */
@DatabaseView(
    viewName = "last_message_view_per_chat",
    value = """
        SELECT m1.*
        FROM chatmessageentity m1
        JOIN (
            SELECT chatId, MAX(timestamp) AS max_timestamp
            FROM chatmessageentity
            GROUP BY chatId
        ) m2 ON m1.chatId = m2.chatId AND m1.timestamp = m2.max_timestamp
    """
)
data class LastMessageView(
    val messageId: String,
    val chatId: String,
    val senderId: String,
    val content: String,
    val timestamp: Long,
    val deliveryStatus: String
)