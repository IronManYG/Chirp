package dev.gaddal.chat.presentation.chat_list_detail

/**
 * Represents actions that can be performed within the Chat List Detail UI.
 * These actions are used to communicate user interactions to the ViewModel
 * and trigger corresponding state updates.
 */
sealed interface ChatListDetailAction {
    data class OnSelectChat(val chatId: String?) : ChatListDetailAction
    data object OnProfileSettingsClick : ChatListDetailAction
    data object OnCreateChatClick : ChatListDetailAction
    data object OnManageChatClick : ChatListDetailAction
    data object OnDismissCurrentDialog : ChatListDetailAction
}