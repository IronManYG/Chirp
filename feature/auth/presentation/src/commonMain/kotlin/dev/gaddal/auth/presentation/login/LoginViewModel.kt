package dev.gaddal.auth.presentation.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_email_not_verified
import chirp.feature.auth.presentation.generated.resources.error_invalid_credentials
import dev.gaddal.auth.domain.EmailValidator
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import dev.gaddal.core.presentation.util.UiText
import dev.gaddal.core.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authService: AuthService,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private val eventChannel = Channel<LoginEvent>()
    val events = eventChannel.receiveAsFlow()

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeTextStates()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    private val isEmailValidFlow =
        snapshotFlow { state.value.emailTextFieldState.text.toString() }
            .map { email -> EmailValidator.validate(email) }
            .distinctUntilChanged()

    private val isPasswordNotBlankFlow =
        snapshotFlow { state.value.passwordTextFieldState.text.toString() }
            .map { it.isNotBlank() }
            .distinctUntilChanged()

    private val isLoggingInFlow = state
        .map { it.isLoggingIn }
        .distinctUntilChanged()

    /**
     * Observes changes in the state of text input fields and updates the view model's state
     * to reflect whether the user can log in.
     *
     * This method listens to three flows:
     * - `isEmailValidFlow`: Indicates if the entered email is valid.
     * - `isPasswordNotBlankFlow`: Indicates if the password field is not blank.
     * - `isLoggingInFlow`: Indicates if a login process is currently in progress.
     *
     * The `canLogin` property in the state is updated based on the following conditions:
     * - Email is valid (`isEmailValidFlow` is `true`).
     * - Password is not blank (`isPasswordNotBlankFlow` is `true`).
     * - The user is not in the middle of logging (`isLoggingInFlow` is `false`).
     *
     * The updated state ensures that the login button's enabled state reacts dynamically to
     * changes in user inputs or ongoing processes.
     */
    private fun observeTextStates() {
        combine(
            isEmailValidFlow,
            isPasswordNotBlankFlow,
            isLoggingInFlow
        ) { isEmailValid, isPasswordNotBlank, isLoggingIn ->
            _state.update {
                it.copy(
                    canLogin = !isLoggingIn && isEmailValid && isPasswordNotBlank
                )
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Handles various actions performed on the login screen, triggering specific behaviors
     * or UI interactions based on the provided action type.
     *
     * @param action An instance of [LoginAction] representing the type of action to handle. Possible actions include:
     * - [LoginAction.OnLoginClick]: Initiates the login process.
     * - [LoginAction.OnTogglePasswordVisibility]: Toggles the visibility of the password input field.
     */
    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginClick -> login()
            LoginAction.OnTogglePasswordVisibility -> {
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
     * Initiates the login process for the user if the current state allows login.
     *
     * This method validates whether the user can initiate login based on the current
     * state of the email and password fields within the `state`. If login is permitted,
     * it launches a coroutine to handle the authentication process asynchronously:
     *
     * 1. Updates the state to indicate that the login process has started.
     * 2. Fetches the values of the email and password from the state.
     * 3. Calls the `authService.login` method to attempt authentication using the obtained credentials.
     * 4. On successful authentication:
     *    - Stores the auth info in session storage
     *    - Updates the state to indicate the login process has ended.
     *    - Sends a `LoginEvent.Success` event to notify subscribers of the successful login.
     * 5. On failure:
     *    - Identifies the type of error encountered.
     *    - Updates the state to reflect the error message and indicate the login process has ended.
     *
     * The method ensures the user is not allowed to initiate a second login while a login request
     * is already in progress by leveraging the `isLoggingIn` flag within the state.
     */
    private fun login() {
        if (!state.value.canLogin) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoggingIn = true
                )
            }

            val email = state.value.emailTextFieldState.text.toString()
            val password = state.value.passwordTextFieldState.text.toString()

            authService
                .login(
                    email = email,
                    password = password
                )
                .onSuccess { authInfo ->
                    sessionStorage.set(authInfo)

                    _state.update {
                        it.copy(
                            isLoggingIn = false
                        )
                    }
                    eventChannel.send(LoginEvent.Success)
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        DataError.Remote.UNAUTHORIZED -> UiText.Resource(Res.string.error_invalid_credentials)
                        DataError.Remote.FORBIDDEN -> UiText.Resource(Res.string.error_email_not_verified)
                        else -> error.toUiText()
                    }

                    _state.update {
                        it.copy(
                            error = errorMessage,
                            isLoggingIn = false
                        )
                    }
                }
        }
    }

}