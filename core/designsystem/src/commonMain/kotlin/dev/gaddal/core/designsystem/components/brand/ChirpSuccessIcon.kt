package dev.gaddal.core.designsystem.components.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.success_checkmark
import dev.gaddal.core.designsystem.theme.extended
import org.jetbrains.compose.resources.vectorResource

/**
 * A composable function to display a success icon with the Chirp design system.
 *
 * @param modifier A [Modifier] to be applied to customize the appearance and placement of the icon.
 * Defaults to an empty modifier.
 */
@Composable
fun ChirpSuccessIcon(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(Res.drawable.success_checkmark),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.extended.success,
        modifier = modifier
    )
}