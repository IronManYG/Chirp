package dev.gaddal.auth.presentation.login

/**
 * Represents actions that can be triggered on the login screen to interact with the UI or handle specific behaviors.
 */
sealed interface LoginAction {
    data object OnTogglePasswordVisibility: LoginAction
    data object OnForgotPasswordClick: LoginAction
    data object OnLoginClick: LoginAction
    data object OnSignUpClick: LoginAction
}