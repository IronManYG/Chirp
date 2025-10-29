package dev.gaddal.auth.presentation.email_verification

/**
 * Represents the state of the email verification process in the application.
 * This state is used to determine and update the UI based on the current status
 * of the verification process.
 *
 * @property isVerifying Indicates whether the email verification process is currently active.
 * @property isVerified Indicates whether the email has been successfully verified.
 */
data class EmailVerificationState(
    val isVerifying: Boolean = false,
    val isVerified: Boolean = false
)