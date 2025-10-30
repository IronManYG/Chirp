package dev.gaddal.auth.presentation.language.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chirp.feature.auth.presentation.generated.resources.Res
import chirp.feature.auth.presentation.generated.resources.language_label_ar
import chirp.feature.auth.presentation.generated.resources.language_label_en
import dev.gaddal.core.designsystem.theme.ChirpTheme
import dev.gaddal.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun LanguageRow(
    code: String,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onSelect
        )
        val label = when (code.lowercase()) {
            "en" -> stringResource(Res.string.language_label_en)
            "ar" -> stringResource(Res.string.language_label_ar)
            else -> code
        }
        Text(
            text = label,
            color = if (selected) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.extended.textPlaceholder
            },
            modifier = Modifier.padding(start = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageRowSelectedPreview() {
    ChirpTheme {
        LanguageRow(code = "en", selected = true, onSelect = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun LanguageRowUnselectedPreview() {
    ChirpTheme {
        LanguageRow(code = "ar", selected = false, onSelect = {})
    }
}
