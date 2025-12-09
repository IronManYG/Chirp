package dev.gaddal.core.presentation.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import dev.icerock.moko.permissions.compose.BindEffect
import dev.icerock.moko.permissions.compose.rememberPermissionsControllerFactory

/**
 * Creates and remembers an instance of [PermissionController] for managing application permissions.
 *
 * This method initializes a platform-specific permissions controller using the moko-permissions library
 * and binds its lifecycle to the current composable hierarchy.
 *
 * @return An instance of [PermissionController] to manage permission requests and states.
 */
@Composable
actual fun rememberPermissionController(): PermissionController {
    val factory = rememberPermissionsControllerFactory()
    val mokoController = remember {
        factory.createPermissionsController()
    }

    BindEffect(mokoController)

    return remember {
        PermissionController(mokoController)
    }
}