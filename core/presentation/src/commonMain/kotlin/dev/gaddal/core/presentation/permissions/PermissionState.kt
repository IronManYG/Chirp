package dev.gaddal.core.presentation.permissions

import dev.gaddal.core.presentation.permissions.PermissionState.DENIED
import dev.gaddal.core.presentation.permissions.PermissionState.GRANTED
import dev.gaddal.core.presentation.permissions.PermissionState.NOT_DETERMINED
import dev.gaddal.core.presentation.permissions.PermissionState.PERMANENTLY_DENIED


/**
 * Represents the current state of a permission within the application.
 *
 * This enum tracks whether a permission has been granted, denied, or requires further action
 * by the user. It is used in conjunction with the [PermissionController] to manage application permissions.
 *
 * States:
 * - [GRANTED]: The permission has been granted by the user.
 * - [DENIED]: The permission request was denied by the user.
 * - [PERMANENTLY_DENIED]: The permission was denied permanently and cannot be requested again.
 * - [NOT_DETERMINED]: The permission state is not yet determined and may require further action.
 *
 * @see PermissionController
 * @see Permission
 */
enum class PermissionState {
    GRANTED,
    DENIED,
    PERMANENTLY_DENIED,
    NOT_DETERMINED
}