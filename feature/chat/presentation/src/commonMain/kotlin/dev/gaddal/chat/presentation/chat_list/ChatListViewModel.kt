package dev.gaddal.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gaddal.chat.domain.chat.ChatRepository
import dev.gaddal.chat.presentation.mappers.toUi
import dev.gaddal.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val repository: ChatRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ChatListState())
    val state = combine(
        _state,
        repository.getChats(),
        sessionStorage.observeAuthInfo()
    ) { currentState, chats, authInfo ->
        if (authInfo == null) {
            return@combine ChatListState()
        }

        currentState.copy(
            chats = chats.map { it.toUi(authInfo.user.id) },
            localParticipant = authInfo.user.toUi()
        )
    }
        .onStart {
            if (!hasLoadedInitialData) {
                loadChats()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ChatListState()
        )

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnChatClick -> {
                _state.update {
                    it.copy(
                        selectedChatId = action.chat.id
                    )
                }
            }

            else -> Unit
        }
    }

    /**
     * Initiates the process of loading chats by invoking the repository to fetch updated chat data.
     *
     * This function is executed within the `viewModelScope` to ensure it is lifecycle-aware
     * and can be safely cancelled if the `ViewModel` is cleared. It performs a network operation
     * to retrieve the most recent list of chats from a remote data source via the repository.
     * The resulting data is handled to update the relevant state in the `ViewModel`.
     */
    private fun loadChats() {
        viewModelScope.launch {
            repository.fetchChats()
        }
    }
}