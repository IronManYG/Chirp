package dev.gaddal.core.designsystem.components.layouts

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.gaddal.core.designsystem.components.brand.ChirpFailureIcon
import dev.gaddal.core.designsystem.components.brand.ChirpSuccessIcon
import dev.gaddal.core.designsystem.components.buttons.ChirpButton
import dev.gaddal.core.designsystem.components.buttons.ChirpButtonStyle
import dev.gaddal.core.designsystem.theme.ChirpTheme
import dev.gaddal.core.designsystem.theme.extended
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable function that provides a simple success layout with a title, description, icon, and buttons.
 *
 * @param title The title text to display in the layout.
 * @param description The description text to display below the title.
 * @param icon A composable function to display an icon at the top of the layout.
 * @param primaryButton A composable function for the primary button that appears below the description.
 * @param secondaryButton An optional composable function for the secondary button, placed below the primary button. Defaults to null.
 * @param modifier A [Modifier] to be applied to the layout. Defaults to an empty modifier.
 */
@Composable
fun ChirpSimpleResultLayout(
    title: String,
    description: String,
    icon: @Composable ColumnScope.() -> Unit,
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable (() -> Unit)? = null,
    secondaryError: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        icon()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -(25).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            primaryButton()

            if(secondaryButton != null) {
                Spacer(modifier = Modifier.height(8.dp))
                secondaryButton()
                if(secondaryError != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = secondaryError,
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
@Preview
fun ChirpSimpleSuccessLayoutPreview() {
    ChirpTheme(darkTheme = true) {
        ChirpSimpleResultLayout(
            title = "Hello world!",
            description = "Test description",
            icon = {
                ChirpSuccessIcon()
            },
            primaryButton = {
                ChirpButton(
                    text = "Log In",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = "Resend verification email",
                    onClick = {},
                    style = ChirpButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        )
    }
}

@Composable
@Preview
fun ChirpSimpleErrorLayoutPreview() {
    ChirpTheme(darkTheme = true) {
        ChirpSimpleResultLayout(
            title = "Hello world!",
            description = "Test description",
            icon = {
                Spacer(modifier = Modifier.height(32.dp))
                ChirpFailureIcon(
                    modifier = Modifier
                        .size(80.dp)
                )
                Spacer(modifier = Modifier.height(32.dp))
            },
            primaryButton = {
                ChirpButton(
                    text = "Log In",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = "Resend verification email",
                    onClick = {},
                    style = ChirpButtonStyle.SECONDARY,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            secondaryError = "Invalid email address"
        )
    }
}