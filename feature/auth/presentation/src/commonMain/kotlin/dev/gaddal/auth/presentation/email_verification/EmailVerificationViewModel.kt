package dev.gaddal.auth.presentation.email_verification

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val token = savedStateHandle.get<String>("token")

    private val _state = MutableStateFlow(EmailVerificationState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                verifyEmail()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = EmailVerificationState()
        )

    // NO-OP: Actions are purely for navigation
    fun onAction(action: EmailVerificationAction) = Unit

    /**
     * Executes the email verification process for the user.
     *
     * This method performs an asynchronous request to the authentication service
     * using the email verification token. It updates the internal state to reflect
     * the progress and outcome of the verification process. While the operation is
     * pending, the state indicates that verification is in progress. Once the
     * operation completes, the state is updated to reflect whether the email was
     * successfully verified or not.
     *
     * The verification process can succeed or fail depending on the validity of the
     * provided token and the response from the authentication service.
     */
    private fun verifyEmail() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isVerifying = true
                )
            }

            authService
                .verifyEmail(token ?: "Invalid token")
                .onSuccess {
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            isVerified = true
                        )
                    }
                }
                .onFailure { _ ->
                    _state.update {
                        it.copy(
                            isVerifying = false,
                            isVerified = false
                        )
                    }
                }
        }
    }
}