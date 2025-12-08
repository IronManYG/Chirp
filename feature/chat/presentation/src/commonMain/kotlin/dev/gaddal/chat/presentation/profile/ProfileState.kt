package dev.gaddal.chat.presentation.profile

import androidx.compose.foundation.text.input.TextFieldState
import dev.gaddal.core.presentation.util.UiText

/**
 * Represents the state of the profile UI, including user details, input states, upload indicators,
 * visibility toggles, and error handling. This class is primarily used to manage and reflect the
 * changes in the profile screen.
 *
 * @property username The username of the user displayed in the profile.
 * @property userInitials The initials of the user, derived from their name, displayed as a fallback for the profile image.
 * @property profilePictureUrl The URL of the profile picture. Null if no picture is set.
 * @property isUploadingImage A flag indicating whether a profile image upload is in progress.
 * @property isDeletingImage A flag indicating whether a profile image deletion is in progress.
 * @property showDeleteConfirmationDialog A flag indicating whether the delete confirmation dialog is visible.
 * @property imageError Represents any error related to the profile image, such as upload failure.
 * @property emailTextState The state of the email text input field.
 * @property currentPasswordTextState The state of the current password text input field.
 * @property newPasswordTextState The state of the new password text input field.
 * @property isCurrentPasswordVisible A flag indicating the visibility status of the current password input.
 * @property isNewPasswordVisible A flag indicating the visibility status of the new password input.
 * @property isChangingPassword A flag indicating whether the password change process is in progress.
 * @property newPasswordError Represents an error related to the new password field, such as invalid formatting.
 * @property canChangePassword A computed flag indicating whether the password change process can be initiated.
 * @property isPasswordChangeSuccessful A flag indicating whether the password change process was successful.
 */
data class ProfileState(
    val username: String = "",
    val userInitials: String = "--",
    val profilePictureUrl: String? = null,
    val isUploadingImage: Boolean = false,
    val isDeletingImage: Boolean = false,
    val showDeleteConfirmationDialog: Boolean = false,
    val imageError: UiText? = null,
    val emailTextState: TextFieldState = TextFieldState(),
    val currentPasswordTextState: TextFieldState = TextFieldState(),
    val newPasswordTextState: TextFieldState = TextFieldState(),
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isChangingPassword: Boolean = false,
    val newPasswordError: UiText? = null,
    val canChangePassword: Boolean = false,
    val isPasswordChangeSuccessful: Boolean = false
)