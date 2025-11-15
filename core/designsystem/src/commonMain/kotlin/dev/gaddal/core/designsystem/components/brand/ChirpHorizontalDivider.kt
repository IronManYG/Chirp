package dev.gaddal.core.designsystem.components.brand

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A composable function to display a horizontal divider styled with the Chirp design system.
 *
 * @param modifier A [Modifier] to be applied to customize the appearance and placement of the divider.
 * Defaults to an empty modifier.
 */
@Composable
fun ChirpHorizontalDivider(
    modifier: Modifier = Modifier
) {
    HorizontalDivider(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.outline
    )
}