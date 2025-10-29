package dev.gaddal.auth.presentation.register_success

/**
 * Represents events that can occur on the Register Success screen.
 *
 * These events are used to handle interactions or updates resulting from user actions
 * or other state changes after the registration process.
 */
sealed interface RegisterSuccessEvent {
    data object ResendVerificationEmailSuccess: RegisterSuccessEvent
}