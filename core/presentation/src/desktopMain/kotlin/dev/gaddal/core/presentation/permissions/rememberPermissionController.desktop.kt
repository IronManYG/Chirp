package dev.gaddal.core.presentation.permissions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

/**
 * Creates and remembers a platform-specific instance of [PermissionController]
 * within a Jetpack Compose composable scope. This function ensures that the
 * [PermissionController] instance is retained across recompositions and provides
 * a consistent mechanism for managing permissions.
 *
 * @return A [PermissionController] instance that always returns granted permissions since
 * desktop applications typically do not require runtime permissions.
 */
@Composable
actual fun rememberPermissionController(): PermissionController {
    return remember { PermissionController() }
}