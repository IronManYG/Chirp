package dev.gaddal.chat.presentation.chat_detail

import dev.gaddal.core.presentation.util.UiText

/**
 * Represents events related to the chat detail screen, encapsulating different types
 * of events that can occur and need to be handled within the chat details context.
 */
sealed interface ChatDetailEvent {
    data object OnChatLeft : ChatDetailEvent
    data class OnError(val error: UiText) : ChatDetailEvent
}