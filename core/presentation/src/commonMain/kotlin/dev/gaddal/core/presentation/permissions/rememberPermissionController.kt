package dev.gaddal.core.presentation.permissions

import androidx.compose.runtime.Composable

/**
 * Remembers and provides a platform-specific implementation of [PermissionController] to manage
 * permissions in a composable context. This function ensures the [PermissionController]
 * is retained across recompositions and is available for use within Jetpack Compose components.
 *
 * @return A [PermissionController] instance to handle permission-related actions such as
 * requesting permissions and checking their states.
 */
@Composable
expect fun rememberPermissionController(): PermissionController