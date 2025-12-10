package dev.gaddal.core.presentation.permissions

/**
 * A controller for handling permission requests in a platform-specific manner.
 *
 * This class is responsible for managing application-level permissions,
 * determining their states, and processing user interactions for granting or denying permissions.
 *
 * In case of desktop, this class is a placeholder and does not perform any actual permission requests
 * since desktop applications typically do not require runtime permissions.
 */
actual class PermissionController {
    /**
     * Requests the specified permission and returns its resulting state.
     *
     * This function suspends until the permission request is completed.
     *
     * @param permission The permission to request.
     * @return [PermissionState.GRANTED] since desktop applications typically do not require runtime permissions.
     */
    actual suspend fun requestPermission(permission: Permission): PermissionState {
        return PermissionState.GRANTED
    }
}