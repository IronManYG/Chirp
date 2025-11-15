package dev.gaddal.chat.presentation.chat_list

import dev.gaddal.chat.presentation.model.ChatUi

/**
 * Represents the user actions triggered on the Chat List Screen of the application.
 * These actions are used to manage navigation, UI state changes, and user interactions
 * such as clicking on chats, avatars, and control elements like logout or settings.
 */
sealed interface ChatListAction {
    data object OnUserAvatarClick : ChatListAction
    data object OnDismissUserMenu : ChatListAction
    data object OnLogoutClick : ChatListAction
    data object OnConfirmLogout : ChatListAction
    data object OnDismissLogoutDialog : ChatListAction
    data object OnCreateChatClick : ChatListAction
    data object OnProfileSettingsClick : ChatListAction
    data class OnChatClick(val chat: ChatUi) : ChatListAction
}