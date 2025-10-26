package dev.gaddal.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state of the registration screen, encapsulating user inputs, their validation status, and UI-specific state.
 *
 * This data class is used to manage and represent the current state of the fields and UI elements on the registration screen.
 * Each property holds specific information about the user input, validation, error messages, loading state, and form visibility.
 *
 * @property emailTextState Represents the current state of the email input field.
 * @property isEmailValid Indicates whether the email input is valid.
 * @property emailError Represents any validation errors for the email input as a `UiText` instance.
 * @property passwordTextState Represents the current state of the password input field.
 * @property isPasswordValid Indicates whether the password input is valid.
 * @property passwordError Represents any validation errors for the password input as a `UiText` instance.
 * @property usernameTextState Represents the current state of the username input field.
 * @property isUsernameValid Indicates whether the username input is valid.
 * @property usernameError Represents any validation errors for the username input as a `UiText` instance.
 * @property registrationError Represents any errors occurring during the registration process as a `UiText` instance.
 * @property isRegistering Indicates whether the registration process is ongoing.
 * @property canRegister Indicates whether the form can be submitted based on validation and other conditions.
 * @property isPasswordVisible Determines whether the password should be visible or obscured in the input field.
 */
data class RegisterState(
    val emailTextState: TextFieldState = TextFieldState(),
    val isEmailValid: Boolean = false,
    val emailError: UiText? = null,
    val passwordTextState: TextFieldState = TextFieldState(),
    val isPasswordValid: Boolean = false,
    val passwordError: UiText? = null,
    val usernameTextState: TextFieldState = TextFieldState(),
    val isUsernameValid: Boolean = false,
    val usernameError: UiText? = null,
    val registrationError: UiText? = null,
    val isRegistering: Boolean = false,
    val canRegister: Boolean = false,
    val isPasswordVisible: Boolean = false
)