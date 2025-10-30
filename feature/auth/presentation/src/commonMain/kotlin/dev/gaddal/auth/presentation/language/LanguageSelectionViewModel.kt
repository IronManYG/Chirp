package dev.gaddal.auth.presentation.language

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gaddal.core.domain.settings.SettingsStorage
import dev.gaddal.core.presentation.util.LanguageManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LanguageSelectionViewModel(
    private val settingsStorage: SettingsStorage,
    private val languageManager: LanguageManager
) : ViewModel() {

    private val eventChannel = Channel<LanguageSelectionEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(
        LanguageSelectionState(
            supportedLanguages = languageManager.getSupportedLanguages().sorted(),
            selectedCode = languageManager.currentLanguage
        )
    )
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = LanguageSelectionState()
        )

    fun onAction(action: LanguageSelectionAction) {
        when (action) {
            is LanguageSelectionAction.OnSelect -> onSelect(action.code)
            is LanguageSelectionAction.OnConfirmClick -> onConfirm()

            else -> Unit
        }
    }

    /**
     * Updates the selected language code in the state if the provided code is supported.
     *
     * This method checks if the given language code is supported. If the code is valid,
     * it updates the `selectedCode` property in the current state to reflect the new selection.
     *
     * @param code The language code to be selected.
     */
    private fun onSelect(code: String) {
        if (!languageManager.isSupported(code)) return
        _state.update { it.copy(selectedCode = code) }
    }

    /**
     * Confirms the user's selected language and applies it to the application.
     *
     * This method:
     * 1. Retrieves the `selectedCode` from the current state. If no language is selected, it returns early.
     * 2. Updates the state to indicate that the language application process is in progress.
     * 3. Persists the selected language using the `settingsStorage` implementation.
     * 4. Attempts to apply the selected language using the `languageManager`. If successful, it emits a
     *    `LanguageSelectionEvent.Completed` event via the `eventChannel`.
     * 5. Updates the state to reflect the completion of the application process, regardless of success.
     */
    private fun onConfirm() {
        val code = state.value.selectedCode ?: return
        viewModelScope.launch {
            _state.update { it.copy(isApplying = true) }
            // 1) persist
            settingsStorage.setLanguage(code)
            // 2) apply
            val ok = languageManager.setLanguage(code)
            _state.update { it.copy(isApplying = false) }
            if (ok) {
                eventChannel.send(LanguageSelectionEvent.Completed)
            }
        }
    }
}
