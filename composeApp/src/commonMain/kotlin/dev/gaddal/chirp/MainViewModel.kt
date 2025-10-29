package dev.gaddal.chirp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gaddal.core.domain.auth.SessionStorage
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val eventChannel = Channel<MainEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MainState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeSession()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MainState()
        )

    private var previousRefreshToken: String? = null

    init {
        viewModelScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().firstOrNull()
            _state.update {
                it.copy(
                    isCheckingAuth = false,
                    isLoggedIn = authInfo != null
                )
            }
        }
    }

    /**
     * Observes and manages the session state by monitoring authentication information updates.
     *
     * This method listens for changes in the authentication state by observing the `AuthInfo` from the `SessionStorage`.
     * If a session expiration is detected (identified by a `null` refresh token when a previous token existed),
     * the session is cleared, the application state is updated to reflect that the user is logged out,
     * and a session expiration event is emitted to the event channel.
     *
     * Key operations include:
     * - Clearing stored authentication data on session expiration.
     * - Updating application state to reflect the session expiration and logged-out status.
     * - Emitting a `MainEvent.OnSessionExpired` event to notify listeners of the session expiration.
     */
    private fun observeSession() {
        sessionStorage
            .observeAuthInfo()
            .onEach { authInfo ->
                val currentRefreshToken = authInfo?.refreshToken
                val isSessionExpired = previousRefreshToken != null && currentRefreshToken == null
                if (isSessionExpired) {
                    sessionStorage.set(null)
                    _state.update {
                        it.copy(
                            isLoggedIn = false
                        )
                    }
                    eventChannel.send(MainEvent.OnSessionExpired)
                }

                previousRefreshToken = currentRefreshToken
            }
            .launchIn(viewModelScope)
    }
}