package dev.gaddal.chat.presentation.profile

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.error_current_password_equal_to_new_one
import chirp.feature.chat.presentation.generated.resources.error_current_password_incorrect
import dev.gaddal.chat.domain.participant.ChatParticipantRepository
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.onFailure
import dev.gaddal.core.domain.util.onSuccess
import dev.gaddal.core.domain.validation.PasswordValidator
import dev.gaddal.core.presentation.util.UiText
import dev.gaddal.core.presentation.util.toUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val authService: AuthService,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val sessionStorage: SessionStorage
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = combine(
        _state,
        sessionStorage.observeAuthInfo()
    ) { currentState, authInfo ->
        if (authInfo != null) {
            currentState.copy(
                username = authInfo.user.username,
                emailTextState = TextFieldState(initialText = authInfo.user.email),
                profilePictureUrl = authInfo.user.profilePictureUrl,
            )
        } else currentState
    }
        .onStart {
            if (!hasLoadedInitialData) {
                observeCanChangePassword()
                fetchLocalParticipantDetails()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnChangePasswordClick -> changePassword()
            is ProfileAction.OnToggleCurrentPasswordVisibility -> toggleCurrentPasswordVisibility()
            is ProfileAction.OnToggleNewPasswordVisibility -> toggleNewPasswordVisibility()
            else -> Unit
        }
    }

    /**
     * Observes changes to the fields required for changing the password and determines whether
     * the user can proceed with the password change. It listens to the current and new password
     * input fields and validates their states to set the `canChangePassword` property in the
     * profile state.
     *
     * This method uses `snapshotFlow` to track the real-time state of password input fields:
     * - The `currentPasswordTextState` is considered valid if it is not blank.
     * - The `newPasswordTextState` is validated using the `PasswordValidator` to ensure it meets
     *   predefined password strength criteria.
     *
     * A `combine` operator is used to merge the validation results for both fields. If both
     * fields are valid, the `canChangePassword` flag in the shared state is updated to true.
     * Otherwise, it remains false.
     *
     * The resulting state updates are launched within the `viewModelScope`.
     */
    private fun observeCanChangePassword() {
        val isCurrentPasswordValidFlow = snapshotFlow {
            state.value.currentPasswordTextState.text.toString()
        }.map { it.isNotBlank() }.distinctUntilChanged()

        val isNewPasswordValidFlow = snapshotFlow {
            state.value.newPasswordTextState.text.toString()
        }.map {
            PasswordValidator.validate(it).isValidPassword
        }.distinctUntilChanged()

        combine(
            isCurrentPasswordValidFlow,
            isNewPasswordValidFlow
        ) { isCurrentValid, isNewValid ->
            _state.update {
                it.copy(
                    canChangePassword = isCurrentValid && isNewValid
                )
            }
        }.launchIn(viewModelScope)
    }

    /**
     * Fetches local participant details for the chat system.
     *
     * This method invokes the `fetchLocalParticipant` function from `chatParticipantRepository`
     * within the `viewModelScope`. The operation is executed as a coroutine, ensuring that
     * it runs asynchronously without blocking the main thread. The fetched participant details
     * are expected to be handled within the repository layer.
     *
     * Note: This function does not directly handle or expose the results of the fetch operation.
     * It relies on the repository to manage the data and any associated state or error handling.
     */
    private fun fetchLocalParticipantDetails() {
        viewModelScope.launch {
            chatParticipantRepository.fetchLocalParticipant()
        }
    }

    /**
     * Initiates the process of changing the user's password.
     *
     * This method validates if the password change can be performed based on the current state flags.
     * If the process is allowed, it proceeds with updating the password through the authentication
     * service using the current and new password inputs. The method also handles success and failure
     * scenarios, updating the UI state accordingly:
     *
     * - On success: Clears password input fields, resets visibility toggles, and marks the change as
     *   successful.
     * - On failure: Updates the state with the appropriate error message (e.g., incorrect current
     *   password or conflict with the new password).
     *
     * The function ensures the loading state is appropriately toggled to reflect the progress of
     * the operation.
     */
    private fun changePassword() {
        if (!state.value.canChangePassword && state.value.isChangingPassword) {
            return
        }

        _state.update {
            it.copy(
                isChangingPassword = true,
                isPasswordChangeSuccessful = false
            )
        }
        viewModelScope.launch {
            val currentPassword = state.value.currentPasswordTextState.text.toString()
            val newPassword = state.value.newPasswordTextState.text.toString()
            authService
                .changePassword(
                    currentPassword = currentPassword,
                    newPassword = newPassword
                )
                .onSuccess {
                    state.value.currentPasswordTextState.clearText()
                    state.value.newPasswordTextState.clearText()

                    _state.update {
                        it.copy(
                            isChangingPassword = false,
                            newPasswordError = null,
                            isNewPasswordVisible = false,
                            isCurrentPasswordVisible = false,
                            isPasswordChangeSuccessful = true
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        DataError.Remote.UNAUTHORIZED -> {
                            UiText.Resource(Res.string.error_current_password_incorrect)
                        }

                        DataError.Remote.CONFLICT -> {
                            UiText.Resource(Res.string.error_current_password_equal_to_new_one)
                        }

                        else -> error.toUiText()
                    }
                    _state.update {
                        it.copy(
                            newPasswordError = errorMessage,
                            isChangingPassword = false
                        )
                    }
                }
        }
    }

    /**
     * Toggles the visibility state of the current password input field in the profile.
     *
     * Updates the `isCurrentPasswordVisible` property within the current state of the profile
     * by inverting its value. This is typically used to show or hide the current password
     * in the profile's password management section.
     */
    private fun toggleCurrentPasswordVisibility() {
        _state.update {
            it.copy(
                isCurrentPasswordVisible = !it.isCurrentPasswordVisible
            )
        }
    }

    /**
     * Toggles the visibility state of the new password input field.
     *
     * This method updates the `_state` property by inverting the current value of
     * `isNewPasswordVisible` in the `ProfileState`. When invoked, it effectively
     * switches the new password input field between visible and hidden states,
     * allowing the user to toggle how the password is displayed on the profile screen.
     *
     * Note: The method assumes the presence of a mutable `_state` property, which
     * encapsulates the profile screen's UI state, and relies on the `copy` function
     * to create an updated state with the modified `isNewPasswordVisible` property.
     */
    private fun toggleNewPasswordVisibility() {
        _state.update {
            it.copy(
                isNewPasswordVisible = !it.isNewPasswordVisible
            )
        }
    }

}