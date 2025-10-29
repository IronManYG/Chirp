package dev.gaddal.auth.presentation.register_success

/**
 * Represents actions that can be performed on the Register Success screen.
 * These actions are triggered by user interactions on the screen.
 */
sealed interface RegisterSuccessAction {
    data object OnLoginClick: RegisterSuccessAction
    data object OnResendVerificationEmailClick: RegisterSuccessAction
}