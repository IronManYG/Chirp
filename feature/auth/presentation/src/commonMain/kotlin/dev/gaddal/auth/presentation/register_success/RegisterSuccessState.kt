package dev.gaddal.auth.presentation.register_success

import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state of the screen displayed after a successful registration.
 *
 * @property registeredEmail The email address of the registered user. Displays the email to notify where
 * the verification email has been sent. Defaults to an empty string.
 * @property isResendingVerificationEmail Indicates whether the process of resending the verification email
 * is currently in progress. Defaults to false.
 * @property resendVerificationError An optional error message, represented as `UiText`,
 * that is displayed if resending the verification email fails.
 */
data class RegisterSuccessState(
    val registeredEmail: String = "",
    val isResendingVerificationEmail: Boolean = false,
    val resendVerificationError: UiText? = null
)