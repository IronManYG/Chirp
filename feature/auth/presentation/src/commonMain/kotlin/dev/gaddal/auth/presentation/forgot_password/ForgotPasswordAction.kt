package dev.gaddal.auth.presentation.forgot_password

/**
 * Represents actions that can occur in the Forgot Password screen,
 * specifically user interactions triggering corresponding UI behavior or logic.
 */
sealed interface ForgotPasswordAction {
    data object OnSubmitClick: ForgotPasswordAction
}