package dev.gaddal.auth.presentation.register_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import dev.gaddal.core.presentation.util.toUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterSuccessViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val eventChannel = Channel<RegisterSuccessEvent>()
    val events = eventChannel.receiveAsFlow()

    private val email = savedStateHandle.get<String>("email")
        ?: throw IllegalStateException("No email passed to register success screen")
    private val _state = MutableStateFlow(
        RegisterSuccessState(
            registeredEmail = email
        )
    )
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
            initialValue = RegisterSuccessState()
        )

    /**
     * Handles user actions performed on the Register Success screen by triggering appropriate logic
     * based on the provided `RegisterSuccessAction`.
     *
     * @param action The user action to be processed, represented as a `RegisterSuccessAction`.
     */
    fun onAction(action: RegisterSuccessAction) {
        when (action) {
            is RegisterSuccessAction.OnResendVerificationEmailClick -> resendVerification()
            else -> Unit
        }
    }

    /**
     * Initiates the process of resending the verification email to the user.
     *
     * This method ensures that only one resend operation can be performed at a time
     * by checking the `isResendingVerificationEmail` property in the current state.
     * If the operation is already in progress, it immediately returns without performing any action.
     *
     * The method updates the UI state to indicate the start and completion of the resend process,
     * and handles any errors that occur during the operation. On success, it sends a
     * `ResendVerificationEmailSuccess` event to the `eventChannel`. On failure, it updates the state
     * with the corresponding error message for user feedback.
     *
     * This functionality is scoped to the `viewModelScope` to ensure proper coroutine lifecycle management.
     */
    private fun resendVerification() {
        if (state.value.isResendingVerificationEmail) {
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isResendingVerificationEmail = true
                )
            }

            authService
                .resendVerificationEmail(email)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isResendingVerificationEmail = false
                        )
                    }
                    eventChannel.send(RegisterSuccessEvent.ResendVerificationEmailSuccess)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isResendingVerificationEmail = false,
                            resendVerificationError = error.toUiText()
                        )
                    }
                }
        }
    }

}