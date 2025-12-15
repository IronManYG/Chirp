package dev.gaddal.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Observes the application's lifecycle to determine its foreground state.
 *
 * This desktop-specific implementation of `AppLifecycleObserver` always considers
 * the application to be in the foreground state. Unlike mobile platforms, desktop
 * applications have fewer restrictions on background processes and resource usage.
 *
 * Key characteristics of desktop implementation:
 * - Always returns true for foreground state
 * - Maintains active WebSocket connections regardless of window focus
 * - No concept of push notifications, so connection management differs from mobile
 *
 * This implementation ensures continuous WebSocket connectivity without the need
 * for connection/disconnection cycles that are common in mobile environments.
 */
actual class AppLifecycleObserver {
    // Always return true to maintain persistent WebSocket connections
    // Desktop platforms have fewer restrictions on background processes
    // and don't require the same connection management as mobile platforms
    actual val isInForeground: Flow<Boolean>
        get() = flowOf(true)
}