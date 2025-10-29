package dev.gaddal.auth.presentation.reset_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.error_reset_password_token_invalid
import chirp.feature.auth.presentation.generated.resources.error_same_password
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import dev.gaddal.core.domain.validation.PasswordValidator
import dev.gaddal.core.presentation.util.UiText
import dev.gaddal.core.presentation.util.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val token = savedStateHandle.get<String>("token")
        ?: throw IllegalStateException("No password reset token")

    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                observeValidationState()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ResetPasswordState()
        )

    private val isPasswordValidFlow = snapshotFlow { state.value.passwordTextState.text.toString() }
        .map { password -> PasswordValidator.validate(password).isValidPassword }
        .distinctUntilChanged()

    /**
     * Observes and updates the validation state for the password input field.
     *
     * This method listens to changes in the password validation state from the `isPasswordValidFlow`
     * and updates the `_state` with the result. Specifically, it updates the `canSubmit` property of
     * the `ResetPasswordState` to determine if the "Submit" button should be enabled.
     *
     * The method relies on Kotlin's flow collection mechanisms, ensuring reactive updates are handled
     * within the `viewModelScope`.
     *
     * This is a private helper method within the `ResetPasswordViewModel` to manage state updates
     * concerned with password validation during the password reset process.
     */
    private fun observeValidationState() {
        isPasswordValidFlow.onEach { isPasswordValid ->
            _state.update {
                it.copy(
                    canSubmit = isPasswordValid
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ResetPasswordAction) {
        when (action) {
            ResetPasswordAction.OnSubmitClick -> resetPassword()
            ResetPasswordAction.OnTogglePasswordVisibilityClick -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }
        }
    }

    /**
     * Resets the user's password by interacting with the authentication service.
     *
     * This method checks the current state to ensure that the password reset operation can
     * proceed. If the reset is allowed (not already in progress and meets submission criteria),
     * it triggers a coroutine to handle the operation asynchronously. The function interacts
     * with the provided `authService` to attempt resetting the password using the new password
     * and token. The resulting state is updated based on the success or failure of the reset
     * process.
     *
     * On a successful reset, the loading state is cleared, the success state is updated, and any
     * previous error messages are removed. On failure, an appropriate error message is displayed,
     * and the loading state is cleared. The function accounts for specific error conditions such
     * as an invalid or expired token and attempts to provide user-friendly feedback.
     *
     * Preconditions:
     * - The function will return early if a reset operation is already in progress or if the
     *   submission criteria are not met.
     * - The new password must be obtained from the current `passwordTextState`, and the token must
     *   be appropriately validated by the `authService`.
     *
     * Side Effects:
     * - Updates the `_state` to reflect changes in the password reset operation's progress, success,
     *   or failure.
     */
    private fun resetPassword() {
        if (state.value.isLoading || !state.value.canSubmit) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isResetSuccessful = false
                )
            }

            val newPassword = state.value.passwordTextState.text.toString()
            authService
                .resetPassword(
                    newPassword = newPassword,
                    token = token
                )
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isResetSuccessful = true,
                            errorText = null
                        )
                    }
                }
                .onFailure { error ->
                    val errorText = when (error) {
                        DataError.Remote.UNAUTHORIZED -> UiText.Resource(Res.string.error_reset_password_token_invalid)
                        DataError.Remote.CONFLICT -> UiText.Resource(Res.string.error_same_password)
                        else -> error.toUiText()
                    }
                    _state.update {
                        it.copy(
                            errorText = errorText,
                            isLoading = false,
                        )
                    }
                }
        }
    }

}