package dev.gaddal.chirp

import dev.gaddal.chirp.windows.WindowState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ApplicationStateHolder(
    private val applicationScope: CoroutineScope
) {

    private val _state = MutableStateFlow(ApplicationState())
    val state = _state
        .onStart {

        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            _state.value
        )

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