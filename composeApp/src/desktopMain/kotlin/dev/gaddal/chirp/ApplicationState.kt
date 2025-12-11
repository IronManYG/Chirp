package dev.gaddal.chirp

import dev.gaddal.chirp.windows.WindowState

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
 */
data class ApplicationState(
    val windows: List<WindowState> = listOf(WindowState())
)