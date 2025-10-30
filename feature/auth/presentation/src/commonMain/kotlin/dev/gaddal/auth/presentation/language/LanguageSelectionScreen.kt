package dev.gaddal.auth.presentation.language

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.language_applying
import chirp.feature.auth.presentation.generated.resources.language_continue
import chirp.feature.auth.presentation.generated.resources.language_select_title
import dev.gaddal.auth.presentation.language.components.LanguageRow
import dev.gaddal.core.designsystem.components.brand.ChirpBrandLogo
import dev.gaddal.core.designsystem.components.buttons.ChirpButton
import dev.gaddal.core.designsystem.components.layouts.ChirpAdaptiveFormLayout
import dev.gaddal.core.designsystem.components.layouts.ChirpSnackbarScaffold
import dev.gaddal.core.designsystem.theme.ChirpTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LanguageSelectionRoot(
    viewModel: LanguageSelectionViewModel = koinViewModel(),
    onCompleted: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LanguageSelectionScreen(
        state = state,
        onAction = viewModel::onAction
    )

    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            if (event is LanguageSelectionEvent.Completed) {
                onCompleted()
            }
        }
    }
}

@Composable
fun LanguageSelectionScreen(
    state: LanguageSelectionState,
    onAction: (LanguageSelectionAction) -> Unit,
) {
    ChirpSnackbarScaffold {
        ChirpAdaptiveFormLayout(
            headerText = stringResource(Res.string.language_select_title),
            logo = { ChirpBrandLogo() },
            modifier = Modifier.fillMaxSize()
        ) {
            state.supportedLanguages.forEach { code ->
                LanguageRow(
                    code = code,
                    selected = state.selectedCode == code,
                    onSelect = { onAction(LanguageSelectionAction.OnSelect(code)) },
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            ChirpButton(
                text = if (state.isApplying) stringResource(Res.string.language_applying) else stringResource(
                    Res.string.language_continue
                ),
                onClick = { onAction(LanguageSelectionAction.OnConfirmClick) },
                enabled = state.selectedCode != null && !state.isApplying,
                isLoading = state.isApplying,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview
@Composable
fun LanguageSelectionScreenPreview() {
    val state = LanguageSelectionState(
        supportedLanguages = listOf("en", "ar"),
        selectedCode = "en"
    )
    ChirpTheme {
        LanguageSelectionScreen(state = state, onAction = {})
    }
}
