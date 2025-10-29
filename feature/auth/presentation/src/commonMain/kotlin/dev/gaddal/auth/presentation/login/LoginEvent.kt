package dev.gaddal.auth.presentation.login

/**
 * Represents various events that can occur during the login process.
 */
sealed interface LoginEvent {
    data object Success: LoginEvent
}