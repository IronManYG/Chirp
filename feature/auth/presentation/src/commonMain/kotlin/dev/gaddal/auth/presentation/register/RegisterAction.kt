package dev.gaddal.auth.presentation.register

/**
 * Represents a set of user actions that can be performed on the register screen.
 *
 * This sealed interface is used to define all possible actions users can take during the
 * registration process. Each action corresponds to a user interaction to handle specific events.
 */
sealed interface RegisterAction {
    data object OnLoginClick : RegisterAction
    data object OnInputTextFocusGain : RegisterAction
    data object OnRegisterClick : RegisterAction
    data object OnTogglePasswordVisibilityClick : RegisterAction
}