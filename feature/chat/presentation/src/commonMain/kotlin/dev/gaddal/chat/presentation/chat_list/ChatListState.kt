package dev.gaddal.chat.presentation.chat_list

import dev.gaddal.chat.presentation.model.ChatUi
import dev.gaddal.core.designsystem.components.avatar.ChatParticipantUi
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state of the chat list screen, including the list of chats, error messages,
 * user actions, and UI loading states.
 *
 * @property chats The list of chats displayed on the screen, represented as [ChatUi]. Defaults to an empty list.
 * @property error Optional error message, represented as [UiText], to display when an issue occurs.
 * @property localParticipant The local user participating in chats, represented as [ChatParticipantUi]. Defaults to null.
 * @property isUserMenuOpen A flag indicating whether the user action menu is currently open. Defaults to false.
 * @property showLogoutConfirmation A flag indicating whether the logout confirmation dialog is visible. Defaults to false.
 * @property selectedChatId The ID of the currently selected chat, or null if no chat is selected.
 * @property isLoading A flag indicating whether the UI is currently in a loading state. Defaults to false.
 */
data class ChatListState(
    val chats: List<ChatUi> = emptyList(),
    val error: UiText? = null,
    val localParticipant: ChatParticipantUi? = null,
    val isUserMenuOpen: Boolean = false,
    val showLogoutConfirmation: Boolean = false,
    val selectedChatId: String? = null,
    val isLoading: Boolean = false,
)