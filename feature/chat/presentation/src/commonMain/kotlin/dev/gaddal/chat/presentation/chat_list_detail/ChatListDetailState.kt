package dev.gaddal.chat.presentation.chat_list_detail

/**
 * Represents the state of the chat list detail screen.
 *
 * @property selectedChatId The ID of the currently selected chat, or null if no chat is selected.
 * @property dialogState The current state of the dialog on the screen.
 */
data class ChatListDetailState(
    val selectedChatId: String? = null,
    val dialogState: DialogState = DialogState.Hidden
)

/**
 * Represents the state of a dialog in the UI. This sealed interface is used to define
 * the visibility and type of dialogs that can be displayed in the application.
 */
sealed interface DialogState {
    data object Hidden : DialogState
    data object CreateChat : DialogState
    data object Profile : DialogState
    data class ManageChat(val chatId: String) : DialogState
}