package dev.gaddal.chat.presentation.components.manage_chat

/**
 * Represents user actions that can be triggered within the Manage Chat feature.
 *
 * This sealed interface is used to define different actions and user interactions
 * that can be performed while managing the process of creating a new chat.
 */
sealed interface ManageChatAction {
    data object OnAddClick : ManageChatAction
    data object OnDismissDialog : ManageChatAction
    data object OnPrimaryActionClick : ManageChatAction
    sealed interface ChatParticipants : ManageChatAction {
        data class OnSelectChat(val chatId: String?) : ManageChatAction
    }
}