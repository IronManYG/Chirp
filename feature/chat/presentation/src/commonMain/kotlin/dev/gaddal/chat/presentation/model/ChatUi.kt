package dev.gaddal.chat.presentation.model

import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.core.designsystem.components.avatar.ChatParticipantUi

/**
 * Represents the user interface model for a chat within the application.
 * It contains details about the participants in the chat, the last message,
 * and other related metadata for display on the UI.
 *
 * @property id A unique identifier for the chat.
 * @property localParticipant The local user participating in the chat, represented as a [ChatParticipantUi].
 * @property otherParticipants A list of other participants in the chat apart from the local user,
 * represented as [ChatParticipantUi].
 * @property lastMessage The last message sent in this chat, represented as a [ChatMessage], or null if no message exists.
 * @property lastMessageSenderUsername The username of the sender of the last message, or null if unavailable.
 */
data class ChatUi(
    val id: String,
    val localParticipant: ChatParticipantUi,
    val otherParticipants: List<ChatParticipantUi>,
    val lastMessage: ChatMessage?,
    val lastMessageSenderUsername: String?
)