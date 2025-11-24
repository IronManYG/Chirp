package dev.gaddal.chat.presentation.model

import dev.gaddal.chat.domain.models.ChatMessageDeliveryStatus
import dev.gaddal.core.designsystem.components.avatar.ChatParticipantUi
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents various UI models for displaying messages in a chat interface.
 * This sealed interface allows for different types of chat messages or separators
 * (e.g., user messages and date markers) to be represented in a type-safe and composable way.
 *
 * @property id A unique identifier for any type of message element.
 */
sealed class MessageUi(open val id: String) {
    /**
     * Represents a message sent by the local user in a chat interface.
     *
     * This data class is used to encapsulate information specific to messages originating
     * from the local user, including contents, status, and UI-related elements.
     *
     * @property id A unique identifier for the message.
     * @property content The text content of the message.
     * @property deliveryStatus The delivery status of the message, represented by [ChatMessageDeliveryStatus].
     * @property formattedSentTime The formatted time of when the message was sent, represented as [UiText].
     */
    data class LocalUserMessage(
        override val id: String,
        val content: String,
        val deliveryStatus: ChatMessageDeliveryStatus,
        val formattedSentTime: UiText
    ) : MessageUi(id)

    /**
     * Represents a chat message sent by another user.
     *
     * This class is part of the `MessageUi` sealed interface and is used to model
     * messages from participants other than the local user within a chat.
     *
     * @property id A unique identifier for the message.
     * @property content The textual content of the message.
     * @property formattedSentTime The formatted time when the message was sent, encapsulated in [UiText].
     * @property sender The participant who sent the message, represented as [ChatParticipantUi].
     */
    data class OtherUserMessage(
        override val id: String,
        val content: String,
        val formattedSentTime: UiText,
        val sender: ChatParticipantUi
    ) : MessageUi(id)

    /**
     * Represents a date separator item in a chat message list UI.
     *
     * This class is used to distinguish messages visually by specific date groups. It contains
     * an identifier for the separator and the date information to display.
     *
     * @property id A unique identifier for the date separator, typically used to differentiate instances.
     * @property date A [UiText] object representing the date to be displayed in the separator.
     */
    data class DateSeparator(
        override val id: String,
        val date: UiText,
    ) : MessageUi(id)
}