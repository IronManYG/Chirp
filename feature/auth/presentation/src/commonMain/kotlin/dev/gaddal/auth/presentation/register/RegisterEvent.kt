
package dev.gaddal.auth.presentation.register

/**
 * Represents events triggered in the registration process.
 *
 * The `RegisterEvent` sealed interface is used for defining events that occur during
 * the user registration flow. These events are typically processed and observed by
 * the ViewModel to handle successful registration outcomes or other pertinent actions.
 *
 * Sealed interfaces allow delimitation of specific events within this flow, improving
 * maintainability and type safety.
 */
sealed interface RegisterEvent {
    data class Success(val email: String): RegisterEvent
}