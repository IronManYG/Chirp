package dev.gaddal.chat.presentation.components.manage_chat

import androidx.compose.foundation.text.input.TextFieldState
import dev.gaddal.core.designsystem.components.avatar.ChatParticipantUi
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state of the Manage Chat feature, holding all the UI-related data necessary for
 * rendering and managing the Manage Chat screen.
 *
 * This class is utilized to maintain the state of the text field for participant search, selected participants,
 * loading indicators, search results, and errors. It provides a structured representation of the data
 * to ensure the consistency and synchronization of the UI.
 *
 * @property queryTextState Represents the state of the text field used for searching chat participants.
 * @property existingChatParticipants Contains a list of existing chat participants.
 * @property selectedChatParticipants Contains a list of participants selected for the new chat.
 * @property isSearching Indicates whether a search operation is currently in progress.
 * @property canAddParticipant Specifies whether the user can currently add a participant.
 * @property currentSearchResult Holds the current search result for a chat participant. It will be null if no result is found.
 * @property searchError Represents any error that occurs during the search for a participant.
 * @property isCreatingChat Indicates whether the chat creation process is currently in progress.
 * @property createChatError Represents any error that occurs during the creation of the chat.
 */
data class ManageChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val existingChatParticipants: List<ChatParticipantUi> = emptyList(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val isSearching: Boolean = false,
    val canAddParticipant: Boolean = false,
    val currentSearchResult: ChatParticipantUi? = null,
    val searchError: UiText? = null,
    val isCreatingChat: Boolean = false,
    val createChatError: UiText? = null
)