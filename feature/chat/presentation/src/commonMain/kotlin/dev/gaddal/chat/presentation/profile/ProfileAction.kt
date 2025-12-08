package dev.gaddal.chat.presentation.profile

/**
 * Represents a collection of actions or events that can be triggered within the profile screen.
 * These actions are used to handle various user interactions and state changes related to the
 * profile, such as managing profile picture upload, toggling password visibility, or handling errors.
 */
sealed interface ProfileAction {
    data object OnDismiss : ProfileAction
    data object OnUploadPictureClick : ProfileAction
    data object OnErrorImagePicker : ProfileAction
    data class OnUriSelected(val uri: String) : ProfileAction
    class OnPictureSelected(val bytes: ByteArray) : ProfileAction
    data object OnDeletePictureClick : ProfileAction
    data object OnConfirmDeleteClick : ProfileAction
    data object OnDismissDeleteConfirmationDialogClick : ProfileAction
    data object OnToggleCurrentPasswordVisibility : ProfileAction
    data object OnToggleNewPasswordVisibility : ProfileAction
    data object OnChangePasswordClick : ProfileAction
}