package dev.gaddal.chat.presentation.create_chat

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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

@OptIn(FlowPreview::class)
class CreateChatViewModel(
    private val chatParticipantService: ChatParticipantService,
    private val chatRepository: ChatRepository
) : ViewModel() {

    private val eventChannel = Channel<CreateChatEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ManageChatState())
    val state = _state
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

    /**
     * Handles user actions related to the Create Chat feature.
     *
     * This method processes the provided action and updates the state or performs
     * specific operations based on the action type.
     *
     * @param action The action performed by the user, represented as a `CreateChatAction`.
     */
    fun onAction(action: ManageChatAction) {
        when (action) {
            ManageChatAction.OnAddClick -> addParticipant()
            ManageChatAction.OnPrimaryActionClick -> createChat()
            else -> Unit
        }
    }

    /**
     * Initiates the creation of a new chat with the currently selected participants.
     *
     * This method retrieves the IDs of the users currently selected as chat participants from the
     * state and triggers the chat creation process if the list of selected participants is not empty.
     * The following operations are performed:
     *
     * 1. Updates the state to indicate that the chat creation process has started, and disables
     *    the ability to add participants.
     * 2. Invokes the `createChat` method of the provided `chatService` with the selected user IDs.
     * 3. Handles the result of the chat creation:
     *    - If successful, updates the state to indicate the completion of chat creation and emits
     *      a `CreateChatEvent.OnChat*/
    private fun createChat() {
        val userIds = state.value.selectedChatParticipants.map { it.id }
        if (userIds.isEmpty()) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSubmitting = true,
                    canAddParticipant = false
                )
            }

            chatRepository
                .createChat(userIds)
                .onSuccess { chat ->
                    _state.update {
                        it.copy(
                            isSubmitting = false
                        )
                    }
                    eventChannel.send(CreateChatEvent.OnChatCreated(chat))
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            submitError = error.toUiText(),
                            canAddParticipant = it.currentSearchResult != null && !it.isSearching,
                            isSubmitting = false
                        )
                    }
                }
        }
    }

    /**
     * Adds the current search result to the list of selected chat participants if it is not already present.
     *
     * This function checks whether the current search result, represented by `state.value.currentSearchResult`,
     * is already included in the list of selected chat participants (`state.value.selectedChatParticipants`).
     * If it is not, the participant is added to the list, and the state is updated accordingly.
     *
     * State updates performed by this function include:
     * - Adding the selected participant to the `selectedChatParticipants` list.
     * - Disabling the ability to add participants by setting `canAddParticipant` to false.
     * - Clearing the `currentSearchResult` to remove the previously selected result.
     * - Resetting the text of the `queryTextState` used for participant search.
     *
     * If `state.value.currentSearchResult` is null, the function will perform no action.
     */
    private fun addParticipant() {
        state.value.currentSearchResult?.let { participant ->
            val isAlreadyPartOfChat = state.value.selectedChatParticipants.any {
                it.id == participant.id
            }
            if (!isAlreadyPartOfChat) {
                _state.update {
                    it.copy(
                        selectedChatParticipants = it.selectedChatParticipants + participant,
                        canAddParticipant = false,
                        currentSearchResult = null
                    )
                }
                _state.value.queryTextState.clearText()
            }
        }
    }

    /**
     * Performs a search operation for a chat participant based on the provided query string.
     *
     * This method updates the current state to reflect the search progress and result. It handles
     * both successful and failed search outcomes and updates the UI state accordingly.
     *
     * @param query The search term used to find a chat participant. If the query is blank, no search is performed.
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