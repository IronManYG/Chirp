package dev.gaddal.auth.presentation.reset_password

/**
 * Represents user actions for resetting a password in the reset password screen.
 *
 * This sealed interface contains actions that can be performed by the user
 * during the password reset process. The actions are typically handled within
 * the ViewModel to update the state of the UI accordingly.
 *
 * @see ResetPasswordState
 * @see ResetPasswordViewModel
 */
sealed interface ResetPasswordAction {
    data object OnSubmitClick: ResetPasswordAction
    data object OnTogglePasswordVisibilityClick: ResetPasswordAction
}