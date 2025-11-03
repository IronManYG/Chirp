package dev.gaddal.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.add
import chirp.feature.chat.presentation.generated.resources.email_or_username
import dev.gaddal.core.designsystem.components.buttons.ChirpButton
import dev.gaddal.core.designsystem.components.buttons.ChirpButtonStyle
import dev.gaddal.core.designsystem.components.textfields.ChirpTextField
import dev.gaddal.core.designsystem.theme.ChirpTheme
import dev.gaddal.core.presentation.util.UiText
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ChatParticipantSearchTextSection(
    queryState: TextFieldState,
    onAddClick: () -> Unit,
    isSearchEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    error: UiText? = null,
    onFocusChanged: (Boolean) -> Unit
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = 20.dp,
                vertical = 16.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ChirpTextField(
            state = queryState,
            modifier = Modifier
                .weight(1f),
            placeholder = stringResource(Res.string.email_or_username),
            title = null,
            supportingText = error?.asString(),
            isError = error != null,
            singleLine = true,
            keyboardType = KeyboardType.Email,
            onFocusChanged = onFocusChanged
        )
        ChirpButton(
            text = stringResource(Res.string.add),
            onClick = onAddClick,
            style = ChirpButtonStyle.SECONDARY,
            enabled = isSearchEnabled,
            isLoading = isLoading,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ChatParticipantSearchTextSectionPreview() {
    ChirpTheme {
        ChatParticipantSearchTextSection(
            queryState = rememberTextFieldState(),
            onAddClick = {},
            isSearchEnabled = true,
            isLoading = false,
            error = null,
            onFocusChanged = {}
        )
    }
}
