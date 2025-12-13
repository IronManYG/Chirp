package dev.gaddal.chirp

import dev.gaddal.chirp.windows.WindowState
import dev.gaddal.core.domain.preferences.ThemePreference
import dev.gaddal.core.domain.preferences.ThemePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ApplicationStateHolder(
    private val applicationScope: CoroutineScope,
    private val themePreferences: ThemePreferences
) {

    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart {
            observeThemePreference()
        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            _state.value
        )

    /**
     * Observes changes to the user's theme preference and updates the application state accordingly.
     *
     * This function listens to the `ThemePreference` flow provided by the `themePreferences` object.
     * It collects emitted values and updates the `themePreference` property within the application state using `_state`.
     *
     * The observation runs within the `applicationScope` to ensure the lifecycle ties to the application's overall scope.
     *
     * Function workflow:
     * - Observes the theme preference from the `themePreferences` object.
     * - Reacts to every emitted `ThemePreference` value and updates the state using the `copy` method.
     * - Operates within the application's coroutine scope, ensuring efficient and lifecycle-aware data management.
     */
    private fun observeThemePreference() {
        themePreferences
            .observeThemePreference()
            .onEach { themePreference ->
                _state.update {
                    it.copy(
                        themePreference = themePreference
                    )
                }
            }
            .launchIn(applicationScope)
    }

    /**
     * Handles the click event for selecting a theme preference.
     *
     * This function updates the application's stored theme preference to reflect
     * the user's selection. It utilizes a coroutine launched in the `applicationScope`
     * to perform the update asynchronously, ensuring smooth execution and lifecycle management.
     *
     * @param themePreference The selected theme preference. It should be one of the
     *                        values defined in the `ThemePreference` enum: `LIGHT`, `DARK`, or `SYSTEM`.
     */
    fun onThemePreferenceClick(themePreference: ThemePreference) {
        applicationScope.launch {
            themePreferences.updateThemePreference(themePreference)
        }
    }

    /**
     * Handles the action of adding a new application window.
     *
     * This function updates the application state by appending a new instance of `WindowState`
     * to the current list of windows in the state. It ensures that a new window with default
     * properties is created and added to the list of active windows.
     *
     * The resulting state reflects the updated list of windows, which is used by the application
     * to render the UI and manage window-related behavior.
     */
    fun onAddWindowClick() {
        _state.update {
            it.copy(
                windows = it.windows + WindowState()
            )
        }
    }

    /**
     * Handles a request to close a specific window by removing it from the application state.
     *
     * This function updates the application's state by filtering out the window with the
     * specified identifier (`id`) from the list of active windows. This effectively
     * represents the closure of the window in the application's state and should typically
     * be called in response to a user-initiated window close event.
     *
     * @param id The unique identifier of the window to be removed from the application state.
     */
    fun onWindowCloseRequest(id: String) {
        _state.update {
            it.copy(
                windows = it.windows.filter { it.id != id }
            )
        }
    }
}