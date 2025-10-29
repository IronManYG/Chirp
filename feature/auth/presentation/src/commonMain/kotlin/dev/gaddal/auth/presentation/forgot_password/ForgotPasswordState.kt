package dev.gaddal.auth.presentation.forgot_password

import androidx.compose.foundation.text.input.TextFieldState
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state of the Forgot Password screen, including UI fields and statuses.
 *
 * @property emailTextFieldState The state of the email text field.
 * @property emailError Displays any error message related to the email input, if present.
 * @property canSubmit Indicates whether the form is in a valid state for submission.
 * @property isLoading Indicates whether a network request or operation is in progress.
 * @property errorText The error message displayed at the top level of the screen.
 * @property isEmailSentSuccessfully Indicates if the "reset password" email has been successfully sent.
 */
data class ForgotPasswordState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val emailError: UiText? = null,
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val isEmailSentSuccessfully: Boolean = false
)