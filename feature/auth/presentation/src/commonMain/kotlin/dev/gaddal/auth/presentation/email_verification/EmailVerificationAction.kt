package dev.gaddal.auth.presentation.email_verification

/**
 * Represents user actions related to email verification flow.
 * This sealed interface is used to handle specific user interactions
 * within the email verification screen and trigger associated events.
 */
sealed interface EmailVerificationAction {
    data object OnLoginClick: EmailVerificationAction
    data object OnCloseClick: EmailVerificationAction
}