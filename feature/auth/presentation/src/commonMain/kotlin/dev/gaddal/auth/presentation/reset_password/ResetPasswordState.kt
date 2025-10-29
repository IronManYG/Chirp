package dev.gaddal.auth.presentation.reset_password

import androidx.compose.foundation.text.input.TextFieldState
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state for the reset password screen in the UI.
 *
 * This data class encapsulates all the relevant state information required
 * for managing the UI elements associated with resetting a password. It is
 * typically used in conjunction with the `ResetPasswordViewModel` and
 * observed in the `ResetPasswordScreen`.
 *
 * @property passwordTextState Holds the current state of the password text field.
 * @property isLoading Indicates whether the password reset operation is currently in progress.
 * @property errorText Represents the error message to display if the reset action fails.
 * @property isPasswordVisible Indicates whether the password text is visible or masked.
 * @property canSubmit Determines if the "Submit" button can be activated based on validation checks.
 * @property isResetSuccessful Indicates whether the password reset process was completed successfully.
 */
data class ResetPasswordState(
    val passwordTextState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val isPasswordVisible: Boolean = false,
    val canSubmit: Boolean = false,
    val isResetSuccessful: Boolean = false
)