package dev.gaddal.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gaddal.chat.domain.chat.ChatRepository
import dev.gaddal.chat.domain.notification.DeviceTokenService
import dev.gaddal.chat.presentation.mappers.toUi
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import dev.gaddal.core.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatListViewModel(
    private val repository: ChatRepository,
    private val sessionStorage: SessionStorage,
    private val deviceTokenService: DeviceTokenService,
    private val authService: AuthService
) : ViewModel() {
    private val eventChannel = Channel<ChatListEvent>()
    val events = eventChannel.receiveAsFlow()

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
            is ChatListAction.OnSelectChat -> {
                _state.update {
                    it.copy(
                        selectedChatId = action.chatId
                    )
                }
            }

            ChatListAction.OnUserAvatarClick -> {
                _state.update {
                    it.copy(
                        isUserMenuOpen = true
                    )
                }
            }

            ChatListAction.OnProfileSettingsClick,
            ChatListAction.OnLogoutClick -> showLogoutConfirmation()

            ChatListAction.OnConfirmLogout -> logout()
            ChatListAction.OnDismissLogoutDialog -> {
                _state.update {
                    it.copy(
                        showLogoutConfirmation = false
                    )
                }
            }

            ChatListAction.OnDismissUserMenu -> {
                _state.update {
                    it.copy(
                        isUserMenuOpen = false
                    )
                }
            }

            else -> Unit
        }
    }

    /**
     * Updates the state to close the user action menu and display the logout confirmation dialog.
     *
     * This method modifies the `ChatListState` by setting `isUserMenuOpen` to `false` and
     * `showLogoutConfirmation` to `true`. It ensures that the logout confirmation dialog
     * is displayed while closing other UI elements like the user menu to prevent overlapping
     * interactions.
     */
    private fun showLogoutConfirmation() {
        _state.update {
            it.copy(
                isUserMenuOpen = false,
                showLogoutConfirmation = true
            )
        }
    }

    /**
     * Handles the logout process for the current user.
     *
     * This method initiates the process of user logout by:
     * - Resetting the UI state to hide the logout confirmation dialog.
     * - Fetching authentication information from the session storage.
     * - Unregistering the device token associated with the user.
     * - Communicating with the authentication service to log out the user by invalidating the refresh token.
     *
     * If the logout process is successful, all local chat data is deleted and a logout success event is emitted.
     * In case of a failure during this process, a logout error event is emitted with the corresponding error message.
     *
     * The function operates within the `viewModelScope` to perform asynchronous tasks and ensures proper lifecycle management.
     */
    private fun logout() {
        _state.update {
            it.copy(
                showLogoutConfirmation = false
            )
        }

        viewModelScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().first()
            val refreshToken = authInfo?.refreshToken ?: return@launch

            deviceTokenService
                .unregisterToken(refreshToken)
                .onSuccess {
                    authService
                        .logout(refreshToken)
                        .onSuccess {
                            sessionStorage.set(null)
                            repository.deleteAllChats()
                            eventChannel.send(ChatListEvent.OnLogoutSuccess)
                        }
                        .onFailure { error ->
                            eventChannel.send(ChatListEvent.OnLogoutError(error.toUiText()))
                        }
                }
                .onFailure { error ->
                    eventChannel.send(ChatListEvent.OnLogoutError(error.toUiText()))
                }
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