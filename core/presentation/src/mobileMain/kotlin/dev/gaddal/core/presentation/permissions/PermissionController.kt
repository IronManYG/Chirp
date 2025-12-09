package dev.gaddal.core.presentation.permissions

import dev.icerock.moko.permissions.DeniedAlwaysException
import dev.icerock.moko.permissions.DeniedException
import dev.icerock.moko.permissions.PermissionsController
import dev.icerock.moko.permissions.RequestCanceledException
import dev.icerock.moko.permissions.notifications.REMOTE_NOTIFICATION

/**
 * A platform-specific implementation of the [PermissionController] class, which manages
 * permission requests and the state of permissions within the application.
 *
 * This class provides a concrete implementation that interacts with the moko-permissions library
 * to handle permission-related operations. It translates the [Permission] enum to moko-permissions
 * representations and processes the resulting states.
 *
 * @constructor Initializes the [PermissionController] with an instance of [PermissionsController].
 *
 * @param mokoPermissionsController A platform-specific permissions controller
 *                                   from the moko-permissions library.
 *
 * @see Permission
 * @see PermissionState
 */
actual class PermissionController(
    private val mokoPermissionsController: PermissionsController
) {
    /**
     * Requests the specified permission from the user.
     *
     * This method attempts to provide the requested permission. If the permission is granted,
     * the state will be updated accordingly. If the permission is denied or permanently denied,
     * the corresponding state is returned.
     *
     * The permission request process involves handling multiple possible exceptions to determine
     * the final permission state based on user actions.
     *
     * @param permission The specific permission being requested.
     * @return [PermissionState] indicating whether the given permission was granted, denied,
     *         or permanently denied by the user.
     */
    actual suspend fun requestPermission(permission: Permission): PermissionState {
        return try {
            mokoPermissionsController.providePermission(permission.toMokoPermission())
            PermissionState.GRANTED
        } catch (_: DeniedAlwaysException) {
            PermissionState.PERMANENTLY_DENIED
        } catch (_: DeniedException) {
            PermissionState.DENIED
        } catch (_: RequestCanceledException) {
            PermissionState.DENIED
        }
    }
}

/**
 * Maps a [Permission] instance to its corresponding [dev.icerock.moko.permissions.Permission] value.
 *
 * This function is used to convert permission enums within the application to
 * those recognized by the Moko Permissions library, enabling interoperability
 * between the application's permission system and the Moko library.
 *
 * @return The equivalent [dev.icerock.moko.permissions.Permission] value for the given [Permission].
 */
fun Permission.toMokoPermission(): dev.icerock.moko.permissions.Permission {
    return when (this) {
        Permission.NOTIFICATIONS -> dev.icerock.moko.permissions.Permission.REMOTE_NOTIFICATION
    }
}