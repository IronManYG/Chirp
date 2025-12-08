@file:OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)

package dev.gaddal.chat.presentation.manage_chat

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.error_participant_not_found
import dev.gaddal.chat.domain.chat.ChatRepository
import dev.gaddal.chat.domain.participant.ChatParticipantService
import dev.gaddal.chat.presentation.components.manage_chat.ManageChatAction
import dev.gaddal.chat.presentation.components.manage_chat.ManageChatState
import dev.gaddal.chat.presentation.mappers.toUi
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import dev.gaddal.core.presentation.util.UiText
import dev.gaddal.core.presentation.util.toUiText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class ManageChatViewModel(
    private val chatRepository: ChatRepository,
    private val chatParticipantService: ChatParticipantService
) : ViewModel() {

    private val eventChannel = Channel<ManageChatEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _chatId = MutableStateFlow<String?>(null)

    private val _state = MutableStateFlow(ManageChatState())
    val state = _chatId
        .flatMapLatest { chatId ->
            if (chatId != null) {
                chatRepository.getActiveParticipantsByChatId(chatId)
            } else emptyFlow()
        }
        .combine(_state) { participants, currentState ->
            currentState.copy(
                existingChatParticipants = participants.map { it.toUi() }
            )
        }
        .onStart {
            if (!hasLoadedInitialData) {
                searchFlow.launchIn(viewModelScope)
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ManageChatState()
        )

    private val searchFlow = snapshotFlow { _state.value.queryTextState.text.toString() }
        .debounce(1.seconds)
        .onEach { query ->
            performSearch(query)
        }

    fun onAction(action: ManageChatAction) {
        when (action) {
            ManageChatAction.OnAddClick -> addParticipant()
            ManageChatAction.OnPrimaryActionClick -> addParticipantsToChat()
            is ManageChatAction.ChatParticipants.OnSelectChat -> {
                _chatId.update { action.chatId }
            }

            else -> Unit
        }
    }

    /**
     * Adds a participant to the list of selected chat participants.
     *
     * This method checks if the current search result for a chat participant is valid and ensures the
     * participant is neither already selected nor part of the existing chat participants before adding
     * them to the list of selected participants. It updates the relevant state and clears the query text
     * used for searching participants.
     *
     * Behavior:
     * - If the participant is already in the list of selected participants or already in the chat,
     *   the participant is not added again.
     * - After successfully adding a participant, the query text state is cleared, and the capability
     *   to add a participant is disabled until a new search result is selected.
     *
     * Updates `state` with:
     * - The updated list of selected participants.
     * - The `canAddParticipant` flag set to `false`.
     * - The `currentSearchResult` set to `null`.
     */
    private fun addParticipant() {
        state.value.currentSearchResult?.let { participantFromSearch ->
            val isAlreadySelected = state.value.selectedChatParticipants.any {
                it.id == participantFromSearch.id
            }
            val isAlreadyInChat = state.value.existingChatParticipants.any {
                it.id == participantFromSearch.id
            }
            val updatedParticipants = if (isAlreadyInChat || isAlreadySelected) {
                state.value.selectedChatParticipants
            } else state.value.selectedChatParticipants + participantFromSearch

            state.value.queryTextState.clearText()
            _state.update {
                it.copy(
                    selectedChatParticipants = updatedParticipants,
                    canAddParticipant = false,
                    currentSearchResult = null
                )
            }
        }
    }

    /**
     * Adds selected participants to the specified chat.
     *
     * This method verifies the presence of selected participants and a valid chat ID before
     * attempting to add participants to a chat. It executes a coroutine to interact with
     * the chat repository, handling both successful and failed outcomes appropriately.
     *
     * In the event of success, an `OnMembersAdded` event is sent through the `eventChannel`.
     * In the case of failure, the state is updated to reflect the error and any ongoing
     * submission process is terminated.
     *
     * The method ensures no operation is performed if no participants are selected or if
     * the chat ID is null.
     */
    private fun addParticipantsToChat() {
        if (state.value.selectedChatParticipants.isEmpty()) {
            return
        }

        val chatId = _chatId.value ?: return

        val selectedParticipants = state.value.selectedChatParticipants
        val selectedUserIds = selectedParticipants.map { it.id }

        viewModelScope.launch {
            chatRepository
                .addParticipantsToChat(
                    chatId = chatId,
                    userIds = selectedUserIds
                )
                .onSuccess {
                    eventChannel.send(ManageChatEvent.OnMembersAdded)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isSubmitting = false,
                            submitError = error.toUiText()
                        )
                    }
                }
        }
    }

    /**
     * Performs a search operation for a chat participant based on the provided query string.
     *
     * This function updates the state of the search process, handles errors, and processes
     * the search results. If the query is blank, it immediately resets the search state. Otherwise,
     * it initiates an asynchronous search using the `chatParticipantService`.
     *
     * @param query The search query string used to find a chat participant. Typically represents a participant's username or identifier.
     */
    private fun performSearch(query: String) {
        if (query.isBlank()) {
            _state.update {
                it.copy(
                    currentSearchResult = null,
                    canAddParticipant = false,
                    searchError = null
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSearching = true,
                    canAddParticipant = false
                )
            }

            chatParticipantService
                .searchParticipant(query)
                .onSuccess { participant ->
                    _state.update {
                        it.copy(
                            currentSearchResult = participant.toUi(),
                            isSearching = false,
                            canAddParticipant = true,
                            searchError = null
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        DataError.Remote.NOT_FOUND -> UiText.Resource(Res.string.error_participant_not_found)
                        else -> error.toUiText()
                    }
                    _state.update {
                        it.copy(
                            searchError = errorMessage,
                            isSearching = false,
                            canAddParticipant = false,
                            currentSearchResult = null
                        )
                    }
                }
        }
    }
}