package dev.gaddal.chirp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.settings.SettingsStorage
import dev.gaddal.core.presentation.util.LanguageManager
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
    private val sessionStorage: SessionStorage,
    private val settingsStorage: SettingsStorage,
    private val languageManager: LanguageManager
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
        loadInitialAuthState()
        loadAndApplyInitialLanguage()
        observeLanguageSettings()
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

    /**
     * Changes the application's language based on the provided language code.
     *
     * The method updates the storage with the new language code, attempts to change
     * the language through the language manager, and if successful, updates the
     * application's state with the current language.
     *
     * @param code The language code to switch to (e.g., "en" for English, "ar" for Arabic).
     */
    fun changeLanguage(code: String) {
        viewModelScope.launch {
            settingsStorage.setLanguage(code)
            val ok = languageManager.setLanguage(code)
            if (ok) {
                _state.update { it.copy(
                    currentLanguage = languageManager.currentLanguage,
                    hasChosenLanguage = true
                ) }
            }
        }
    }

    /**
     * Loads the initial authentication state and updates the application state accordingly.
     *
     * This method observes the authentication information from the `SessionStorage` to determine
     * if the user is currently logged in. It listens for the first available authentication record,
     * then updates the application state (`_state`) to reflect whether the user is authenticated
     * or not. Additionally, it updates the `isCheckingAuth` flag to indicate the completion of
     * the authentication check process.
     *
     * Key behaviors:
     * - Observes authentication information using a flow with `firstOrNull()`.
     * - If authentication information is present, the user is considered logged in.
     * - Updates the application state to stop checking authentication and set the logged-in status.
     */
    private fun loadInitialAuthState() {
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
     * Loads and applies the initial language settings for the application.
     *
     * This function observes the user's settings stored in `SettingsStorage` to determine their preferred language.
     * If no initial language is found, the application defaults to the first supported language provided by the `LanguageManager`.
     * Once resolved, the language is set using the `LanguageManager` and the application state is updated to reflect the language change.
     *
     * Key operations include:
     * - Observing settings to check for an existing language selection.
     * - Determining the initial language to be applied.
     * - Applying the language using `LanguageManager`.
     * - Updating the application state to indicate the language initialization is complete and whether the user has chosen a language.
     */
    private fun loadAndApplyInitialLanguage() {
        viewModelScope.launch {
            val initial = settingsStorage.observeSettings().firstOrNull()?.languageCode
            val initCode = (initial ?: languageManager.getSupportedLanguages().first())
            languageManager.setLanguage(initCode)
            _state.update {
                it.copy(
                    isCheckingLanguage = false,
                    currentLanguage = languageManager.currentLanguage,
                    hasChosenLanguage = initial != null
                )
            }
        }
    }

    /**
     * Observes changes in the application's language settings and updates the state accordingly.
     *
     * This method listens for updates from the `SettingsStorage` by collecting the emitted
     * `AppSettings` flow and checks whether a language code has been chosen. The result is
     * used to update the application's state by setting the `hasChosenLanguage` field
     * to `true` if a language code exists, or `false` otherwise.
     *
     * Key responsibilities:
     * - Monitor changes in language-related settings.
     * - Update the `MainState` to reflect if the user has selected a language.
     */
    private fun observeLanguageSettings() {
        viewModelScope.launch {
            settingsStorage.observeSettings().collect { appSettings ->
                _state.update { it.copy(hasChosenLanguage = appSettings.languageCode != null) }
            }
        }
    }
}