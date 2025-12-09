package dev.gaddal.chat.presentation.chat_list

import dev.gaddal.core.presentation.util.UiText

/**
 * Represents events related to the chat list that occur during the session. These events
 * are often the result of changes in user state or error handling scenarios within the chat list screen.
 */
sealed interface ChatListEvent {
    data object OnLogoutSuccess : ChatListEvent
    data class OnLogoutError(val error: UiText) : ChatListEvent
}