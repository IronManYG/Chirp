package dev.gaddal.chirp

/**
 * Represents events related to the main application lifecycle or state changes.
 *
 * This sealed interface is used to define various types of events that can occur
 * during the application's operation. It provides a structured way to handle
 * and respond to specific events, such as session expiration.
 *
 * Events that implement this interface can be emitted and observed by other components,
 * such as the application's ViewModel, to trigger appropriate actions or state updates.
 *
 * Subtypes:
 * - `OnSessionExpired`: Indicates that the user's session has expired, which may require
 *   navigating to an authentication screen or handling user logout.
 */
sealed interface MainEvent {
    data object OnSessionExpired: MainEvent
}