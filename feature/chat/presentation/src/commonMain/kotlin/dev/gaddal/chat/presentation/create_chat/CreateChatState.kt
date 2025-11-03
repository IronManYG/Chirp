package dev.gaddal.chat.presentation.create_chat

import androidx.compose.foundation.text.input.TextFieldState
import dev.gaddal.core.designsystem.components.avatar.ChatParticipantUi
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state of the Create Chat feature, holding all the UI-related data necessary for
 * rendering and managing the Create Chat screen.
 *
 * This class is utilized to maintain the state of the text field for participant search, selected participants,
 * loading indicators, search results, and errors. It provides a structured representation of the data
 * to ensure the consistency and synchronization of the UI.
 *
 * @property queryTextState Represents the state of the text field used for searching chat participants.
 * @property selectedChatParticipants Contains a list of participants selected for the new chat.
 * @property isAddingParticipant Indicates whether a participant is being added, typically used to display a loading indicator.
 * @property isLoadingParticipants Indicates whether participants are being loaded during a search operation.
 * @property canAddParticipant Specifies whether the user can currently add a participant.
 * @property currentSearchResult Holds the current search result for a chat participant. It will be null if no result is found.
 * @property searchError Represents any error that occurs during the search for a participant.
 * @property isCreatingChat Indicates whether the chat creation process is currently in progress.
 */
data class CreateChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val isAddingParticipant: Boolean = false,
    val isLoadingParticipants: Boolean = false,
    val canAddParticipant: Boolean = false,
    val currentSearchResult: ChatParticipantUi? = null,
    val searchError: UiText? = null,
    val isCreatingChat: Boolean = false,
)