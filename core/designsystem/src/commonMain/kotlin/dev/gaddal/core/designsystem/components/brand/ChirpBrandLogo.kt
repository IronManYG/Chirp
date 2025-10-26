package dev.gaddal.core.designsystem.components.brand

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.logo_chirp
import org.jetbrains.compose.resources.vectorResource

/**
 * A composable function to display the Chirp brand logo as an icon.
 *
 * @param modifier A [Modifier] to be applied to customize the appearance and placement of the logo.
 * Defaults to an empty modifier.
 */
@Composable
fun ChirpBrandLogo(
    modifier: Modifier = Modifier
) {
    Icon(
        imageVector = vectorResource(Res.drawable.logo_chirp),
        contentDescription = null,
        tint = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )
}