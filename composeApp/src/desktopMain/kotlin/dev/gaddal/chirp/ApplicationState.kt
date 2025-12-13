package dev.gaddal.chirp

import androidx.compose.ui.window.TrayState
import dev.gaddal.chirp.windows.WindowState
import dev.gaddal.core.domain.preferences.ThemePreference

/**
 * Represents the overall state of the application.
 *
 * This data class encapsulates the state of the application, specifically maintaining
 * a list of windows (`windows`) that are currently open. Each window is represented
 * by an instance of `WindowState`.
 *
 * This class is primarily used to manage the state of application windows and serve
 * as a central model for observing or updating the application's state dynamically.
 *
 * @property windows A list of `WindowState` objects representing the currently active
 * windows in the application. By default, it initializes with a single `WindowState`.
 * @property themePreference The current theme preference for the application. Defaults to `ThemePreference.SYSTEM`.
 * @property trayState The state of the application's system tray. Defaults to `TrayState()`.
 */
data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState()),
    val themePreference: ThemePreference = ThemePreference.SYSTEM,
    val trayState: TrayState = TrayState()
)