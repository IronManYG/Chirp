package dev.gaddal.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state of the login screen, encapsulating the current values and UI states
 * for the user authentication process.
 *
 * @property emailTextFieldState The state of the email input field, including its current value and error state.
 * @property passwordTextFieldState The state of the password input field, including its current value and error state.
 * @property isPasswordVisible Indicates whether the password input field should display its text or mask it.
 * @property canLogin Specifies whether the login button is enabled, based on the validity of the input fields.
 * @property isLoggingIn A flag indicating if a login request is currently in progress.
 * @property error Represents an optional error message to be displayed on the screen, if any.
 */
data class LoginState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val isPasswordVisible: Boolean = false,
    val canLogin: Boolean = false,
    val isLoggingIn: Boolean = false,
    val error: UiText? = null
)