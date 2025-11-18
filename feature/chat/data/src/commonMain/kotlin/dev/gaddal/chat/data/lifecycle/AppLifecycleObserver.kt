package dev.gaddal.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow

/**
 * Observes the application's lifecycle to determine its foreground or background state.
 *
 * This class provides a mechanism to monitor the app's visibility to the user by exposing a
 * [Flow] that emits updates about whether the app is currently in the foreground.
 *
 * This can be particularly useful for managing resources or performing actions that must
 * be tied to the app's visibility lifecycle, such as pausing or resuming certain processes
 * when the app enters or exits the foreground.
 */
expect class AppLifecycleObserver {
    val isInForeground: Flow<Boolean>
}