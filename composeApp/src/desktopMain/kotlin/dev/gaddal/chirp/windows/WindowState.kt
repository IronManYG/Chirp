package dev.gaddal.chirp.windows

import java.util.UUID

/**
 * Represents the state of an application window.
 *
 * This data class holds information about individual application windows, including:
 * - A unique identifier `id` for the window.
 * - The window's `title`, defaulting to "Chirp".
 * - Whether the window is currently focused, indicated by `isFocused`.
 *
 * This class is utilized to manage and represent window-related state within the application.
 * Instances are typically used as part of the `ApplicationState` to track multiple windows.
 */
data class WindowState(
    val id: String = UUID.randomUUID().toString(),
    val title: String = "Chirp",
    val isFocused: Boolean = false
)