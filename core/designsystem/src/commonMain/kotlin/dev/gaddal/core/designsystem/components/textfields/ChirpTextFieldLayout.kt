package dev.gaddal.core.designsystem.components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.gaddal.core.designsystem.theme.extended

/**
 * A composable function defining the layout for a text field with optional title, supporting text,
 * custom error handling, and focus state management.
 *
 * @param title Optional title displayed above the text field. Default is null.
 * @param isError Indicates whether the text field is in an error state. Default is false.
 * @param supportingText Optional supporting text displayed below the text field for guidance or error messages. Default is null.
 * @param enabled Indicates whether the text field is enabled for user input. Default is true.
 * @param onFocusChanged A callback invoked when the focus state of the text field changes.
 * @param modifier The [Modifier] to be applied to this layout for styling or layout adjustments. Default is [Modifier].
 * @param textField A composable lambda defining the text field implementation. This lambda takes a [Modifier] for styling
 * and a [MutableInteractionSource] to track interactions such as focus.
 */
@Composable
fun ChirpTextFieldLayout(
    title: String? = null,
    isError: Boolean = false,
    supportingText: String? = null,
    enabled: Boolean = true,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    textField: @Composable (Modifier, MutableInteractionSource) -> Unit
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }
    val isFocused by interactionSource.collectIsFocusedAsState()

    LaunchedEffect(isFocused) {
        onFocusChanged(isFocused)
    }

    val textFieldStyleModifier = Modifier
        .fillMaxWidth()
        .background(
            color = when {
                isFocused -> MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.05f
                )

                enabled -> MaterialTheme.colorScheme.surface
                else -> MaterialTheme.colorScheme.extended.secondaryFill
            },
            shape = RoundedCornerShape(8.dp)
        )
        .border(
            width = 1.dp,
            color = when {
                isError -> MaterialTheme.colorScheme.error
                isFocused -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.outline
            },
            shape = RoundedCornerShape(8.dp)
        )
        .padding(12.dp)

    Column(
        modifier = modifier
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.extended.textSecondary
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        textField(textFieldStyleModifier, interactionSource)

        if (supportingText != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = supportingText,
                color = if (isError) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.extended.textTertiary
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}