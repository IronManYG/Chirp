package dev.gaddal.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.gaddal.core.designsystem.components.buttons.ChirpButton
import dev.gaddal.core.designsystem.components.buttons.ChirpButtonStyle
import dev.gaddal.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ManageChatButtonSection(
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.End)
    ) {
        secondaryButton()
        primaryButton()
    }
}

@Preview(showBackground = true)
@Composable
fun ManageChatButtonSectionPreview() {
    ChirpTheme {
        ManageChatButtonSection(
            primaryButton = {
                ChirpButton(
                    text = "Primary",
                    onClick = {}
                )
            },
            secondaryButton = {
                ChirpButton(
                    text = "Secondary",
                    onClick = {},
                    style = ChirpButtonStyle.SECONDARY
                )
            }
        )
    }
}