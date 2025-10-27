package dev.gaddal.auth.presentation.register_success

/**
 * Represents the state of the screen displayed after a successful registration.
 *
 * @property registeredEmail The email address of the registered user. Displays the email to notify where
 * the verification email has been sent. Defaults to an empty string.
 * @property isResendingVerificationEmail Indicates whether the process of resending the verification email
 * is currently in progress. Defaults to false.
 */
data class RegisterSuccessState(
    val registeredEmail: String = "",
    val isResendingVerificationEmail: Boolean = false
)