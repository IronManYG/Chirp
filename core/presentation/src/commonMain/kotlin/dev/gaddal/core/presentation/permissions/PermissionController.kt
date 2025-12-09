package dev.gaddal.core.presentation.permissions

/**
 * A platform-agnostic controller for managing and requesting permissions in the application.
 *
 * This class is responsible for handling permission requests and determining
 * the current state of permissions, such as whether they are granted, denied, or require user intervention.
 *
 * @see Permission
 * @see PermissionState
 * @see rememberPermissionController
 */
expect class PermissionController {
    /**
     * Requests the specified permission and returns its resulting state.
     *
     * This function suspends until the permission request is completed.
     *
     * @param permission The permission to request. This parameter is of type [Permission].
     * @return A [PermissionState] representing the result of the permission request.
     *         Possible states include:
     *         - [PermissionState.GRANTED]: The permission has been granted.
     *         - [PermissionState.DENIED]: The permission request was denied by the user.
     *         - [PermissionState.PERMANENTLY_DENIED]: The permission was denied and cannot be requested again.
     *         - [PermissionState.NOT_DETERMINED]: The permission state has not been determined.
     */
    suspend fun requestPermission(permission: Permission): PermissionState
}