package dev.gaddal.auth.presentation.forgot_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gaddal.auth.domain.EmailValidator
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
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

class ForgotPasswordViewModel(
    private val authService: AuthService
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ForgotPasswordState())
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
            initialValue = ForgotPasswordState()
        )

    private val isEmailValidFlow = snapshotFlow { state.value.emailTextFieldState.text.toString() }
        .map { email -> EmailValidator.validate(email) }
        .distinctUntilChanged()

    /**
     * Observes the validation state of the email input and updates the UI state accordingly.
     *
     * This function listens to changes in the `isEmailValidFlow` and updates the `_state` field
     * by modifying the `canSubmit` property. Specifically:
     * - If the email is valid (`isEmailValid` is true), `canSubmit` is set to true, allowing form submission.
     * - If the email is invalid (`isEmailValid` is false), `canSubmit` is set to false, disabling form submission.
     *
     * The observation runs within the `viewModelScope`, ensuring it respects the lifecycle of the `ViewModel`.
     */
    private fun observeValidationState() {
        isEmailValidFlow.onEach { isEmailValid ->
            _state.update {
                it.copy(
                    canSubmit = isEmailValid
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            is ForgotPasswordAction.OnSubmitClick -> submitForgotPasswordRequest()
        }
    }

    /**
     * Submits a request to initiate the forgot password process.
     *
     * This function checks whether the request can be submitted by ensuring that the current
     * state is not loading and the form is valid (`canSubmit` is true). If the conditions are met,
     * it sends the request using the `authService` to handle the forgot password process.
     *
     * The function updates the internal state as follows:
     * - Sets `isLoading` to true to indicate that a request is being processed.
     * - Resets any previous error messages (`errorText`) and email success flags.
     *
     * The result of the request is processed as follows:
     * - On success, updates the state to reflect that the email has been sent successfully
     *   (`isEmailSentSuccessfully` set to true) and clears the loading flag.
     * - On failure, updates the state with the error message (`errorText`) derived from the failure
     *   and resets the loading flag.
     *
     * If invoked while already loading or if the form is invalid, the function terminates early
     * and does not perform any action.
     */
    private fun submitForgotPasswordRequest() {
        if (state.value.isLoading || !state.value.canSubmit) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isEmailSentSuccessfully = false,
                    errorText = null
                )
            }

            val email = state.value.emailTextFieldState.text.toString()
            authService
                .forgotPassword(email)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isEmailSentSuccessfully = true,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            errorText = error.toUiText(),
                            isLoading = false
                        )
                    }
                }
        }
    }

}