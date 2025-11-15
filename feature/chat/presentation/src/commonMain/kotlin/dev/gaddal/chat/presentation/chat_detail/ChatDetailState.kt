package dev.gaddal.chat.presentation.chat_detail

import androidx.compose.foundation.text.input.TextFieldState
import dev.gaddal.chat.domain.models.ConnectionState
import dev.gaddal.chat.presentation.model.ChatUi
import dev.gaddal.chat.presentation.model.MessageUi
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state of the chat detail screen, capturing various UI components and their states.
 *
 * @property chatUi The user interface model for the chat, encapsulating participants, metadata, and the last message.
 * @property isLoading Indicates whether the chat detail screen is in a loading state.
 * @property messages A list of messages displayed in the chat UI.
 * @property error Represents any error that may occur, expressed as a localized text.
 * @property messageTextFieldState The state of the message input text field.
 * @property canSendMessage Indicates whether the user can send messages, based on certain validation criteria.
 * @property isPaginationLoading Specifies whether messages are being fetched for pagination purposes.
 * @property paginationError Captures errors that occur during pagination as localized text.
 * @property endReached Denotes whether all the messages in the chat have been loaded.
 * @property bannerState The state of the banner shown within the chat, typically used for contextual information.
 * @property isChatOptionsOpen Indicates if the chat options menu is currently open.
 * @property isNearBottom Tracks whether the user is close to the bottom of the message list, often for UX improvements.
 * @property connectionState The current state of the connection to the chat, e.g., connected, disconnected.
 */
data class ChatDetailState(
    val chatUi: ChatUi? = null,
    val isLoading: Boolean = false,
    val messages: List<MessageUi> = emptyList(),
    val error: UiText? = null,
    val messageTextFieldState: TextFieldState = TextFieldState(),
    val canSendMessage: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val isChatOptionsOpen: Boolean = false,
    val isNearBottom: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.DISCONNECTED
)

/**
 * Represents the state of a banner in the user interface.
 *
 * @property formattedDate The text to be displayed in the banner, optionally using the [UiText] container
 * to support both static and localized content. Defaults to `null` if no text is provided.
 * @property isVisible Indicates whether the banner is currently visible or not. Defaults to `false`.
 */
data class BannerState(
    val formattedDate: UiText? = null,
    val isVisible: Boolean = false
)