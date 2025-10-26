package dev.gaddal.core.designsystem.preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import dev.gaddal.core.designsystem.components.layouts.ChirpAdaptiveResultLayout
import dev.gaddal.core.designsystem.theme.ChirpTheme

/**
 * A preview function for demonstrating the `ChirpAdaptiveResultLayout` composable in different themes
 * (light and dark) and screen sizes. This function showcases an adaptive layout with a result message.
 *
 * The layout adapts to various device configurations like mobile, tablet, and desktop, ensuring a consistent
 * and responsive user interface. It uses `ChirpTheme` for styling and injects a sample content message
 * to illustrate the registration success state.
 *
 * This function is annotated with `@PreviewLightDark` and `@PreviewScreenSizes` to enable rendering previews
 * for both light and dark themes across different screen sizes in the IDE.
 */
@Composable
@PreviewLightDark
@PreviewScreenSizes
fun ChirpAdaptiveResultLayoutPreview() {
    ChirpTheme {
        ChirpAdaptiveResultLayout(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                Text(
                    text = "Registration successful!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        )
    }
}