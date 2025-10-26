package dev.gaddal.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_account_exists
import chirp.feature.auth.presentation.generated.resources.error_invalid_email
import chirp.feature.auth.presentation.generated.resources.error_invalid_password
import chirp.feature.auth.presentation.generated.resources.error_invalid_username
import dev.gaddal.auth.domain.EmailValidator
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import dev.gaddal.core.domain.validation.PasswordValidator
import dev.gaddal.core.presentation.util.UiText
import dev.gaddal.core.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authService: AuthService
) : ViewModel() {

    private val eventChannel = Channel<RegisterEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = RegisterState()
        )

    /**
     * Handles user actions performed on the registration screen by triggering appropriate logic
     * based on the provided `RegisterAction`.
     *
     * @param action The user action to be processed, represented as a `RegisterAction`.
     */
    fun onAction(action: RegisterAction) {
        when (action) {
            RegisterAction.OnLoginClick -> Unit
            RegisterAction.OnRegisterClick -> register()
            RegisterAction.OnTogglePasswordVisibilityClick -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }

            else -> Unit
        }
    }

    /**
     * Handles the user registration process by validating form inputs and communicating with the authentication service.
     *
     * This method performs the following steps:
     * 1. Validates the form inputs (email, username, and password) using the `validateFormInputs` method.
     *    - If validation fails, it stops further execution.
     * 2. Launches a coroutine to handle the asynchronous registration process.
     *    - Updates the current state to indicate that the registration process is in progress.
     *    - Extracts the email, username, and password values from the `state`.
     *    - Calls the `register` method of the `authService` with the extracted credentials.
     * 3. Handles the result of the registration process:
     *    - On success: Updates the state to reflect that the registration has completed successfully and resets UI-related flags.
     *    - On failure: Determines the type of error (e.g., conflict due to an existing account) and updates the state with a descriptive error message
     *  for the user.
     *
     * This method ensures that all state updates are performed on the main thread and keeps the UI responsive during the registration process.
     */
    private fun register() {
        if (validateFormInputs()) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isRegistering = true
                )
            }

            val email = state.value.emailTextState.text.toString()
            val username = state.value.usernameTextState.text.toString()
            val password = state.value.passwordTextState.text.toString()

            authService
                .register(
                    email = email,
                    username = username,
                    password = password
                )
                .onSuccess {
                    _state.update {
                        it.copy(
                            isRegistering = false
                        )
                    }
                }
                .onFailure { error ->
                    val registrationError = when (error) {
                        DataError.Remote.CONFLICT -> UiText.Resource(Res.string.error_account_exists)
                        else -> error.toUiText()
                    }
                    _state.update {
                        it.copy(
                            isRegistering = false,
                            registrationError = registrationError
                        )
                    }
                }
        }
    }

    /**
     * Clears all error states for text fields in the registration form.
     *
     * This method sets the error properties (`emailError`, `usernameError`, `passwordError`, and `registrationError`)
     * in the current state to `null`, effectively removing any displayed error messages for these fields.
     */
    private fun clearAllTextFieldErrors() {
        _state.update {
            it.copy(
                emailError = null,
                usernameError = null,
                passwordError = null,
                registrationError = null
            )
        }
    }

    /**
     * Validates the current form inputs for email, username, and password fields.
     * This method checks the validity of each input field against predefined criteria:
     * - Validates email using `EmailValidator`.
     * - Validates password using `PasswordValidator`.
     * - Checks if the username length is within the allowed range (3 to 20 characters).
     *
     * Any errors found during validation are updated in the state's corresponding error properties,
     * and are displayed to the user.
     *
     * @return `true` if all inputs are valid (email, username, password); `false` otherwise.
     */
    private fun validateFormInputs(): Boolean {
        clearAllTextFieldErrors()

        val currentState = state.value
        val email = currentState.emailTextState.text.toString()
        val username = currentState.usernameTextState.text.toString()
        val password = currentState.passwordTextState.text.toString()

        val isEmailValid = EmailValidator.validate(email)
        val passwordValidationState = PasswordValidator.validate(password)
        val isUsernameValid = username.length in 3..20

        val emailError = if (!isEmailValid) {
            UiText.Resource(Res.string.error_invalid_email)
        } else null
        val usernameError = if (!isUsernameValid) {
            UiText.Resource(Res.string.error_invalid_username)
        } else null
        val passwordError = if (!passwordValidationState.isValidPassword) {
            UiText.Resource(Res.string.error_invalid_password)
        } else null

        _state.update {
            it.copy(
                emailError = emailError,
                usernameError = usernameError,
                passwordError = passwordError
            )
        }

        return isUsernameValid && isEmailValid && passwordValidationState.isValidPassword
    }
}