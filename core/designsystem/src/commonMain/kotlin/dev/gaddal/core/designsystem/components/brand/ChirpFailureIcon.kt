package dev.gaddal.core.designsystem.components.brand

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A composable function to display a failure icon with the Chirp design system.
 *
 * @param modifier A [Modifier] to be applied to customize the appearance and placement of the icon.
 * Defaults to an empty modifier.
 */
@Composable
fun ChirpFailureIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = Icons.Default.Close,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.error,
        modifier = modifier
    )
}