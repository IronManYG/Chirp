package dev.gaddal.core.designsystem.components.textfields

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.eye_icon
import chirp.core.designsystem.generated.resources.eye_off_icon
import chirp.core.designsystem.generated.resources.hide_password
import chirp.core.designsystem.generated.resources.show_password
import dev.gaddal.core.designsystem.theme.ChirpTheme
import dev.gaddal.core.designsystem.theme.extended
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable function that provides a password text field with visibility toggle functionality.
 *
 * @param state The state of the text field, encapsulating the current text value and other
 *              related properties such as selection and value change state.
 * @param isPasswordVisible Boolean flag indicating whether the password is visible or hidden.
 * @param onToggleVisibilityClick A callback invoked when the visibility toggle icon is clicked.
 * @param modifier The [Modifier] to be applied to this text field for layout or styling adjustments. Default is [Modifier].
 * @param placeholder Optional placeholder text displayed when the text field is empty. Default is null.
 * @param title Optional title displayed above the text field. Default is null.
 * @param supportingText Optional supporting text displayed below the text field for guidance or error messages. Default is null.
 * @param isError A flag indicating whether the text field is in an error state. Default is false.
 * @param enabled A flag indicating whether the text field is enabled for user interaction. Default is true.
 * @param onFocusChanged A callback invoked with a boolean indicating whether the text field has gained or lost focus. Default is an empty lambda.
 */
@Composable
fun ChirpPasswordTextField(
    state: TextFieldState,
    isPasswordVisible: Boolean,
    onToggleVisibilityClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    title: String? = null,
    supportingText: String? = null,
    isError: Boolean = false,
    enabled: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {},
) {
    ChirpTextFieldLayout(
        title = title,
        isError = isError,
        supportingText = supportingText,
        enabled = enabled,
        onFocusChanged = onFocusChanged,
        modifier = modifier
    ) { styleModifier, interactionSource ->
        BasicSecureTextField(
            state = state,
            modifier = styleModifier,
            enabled = enabled,
            textObfuscationMode = if (isPasswordVisible) {
                TextObfuscationMode.Visible
            } else TextObfuscationMode.Hidden,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password
            ),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = if (enabled) {
                    MaterialTheme.colorScheme.onSurface
                } else {
                    MaterialTheme.colorScheme.extended.textPlaceholder
                }
            ),
            interactionSource = interactionSource,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
            decorator = { innerBox ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (state.text.isEmpty() && placeholder != null) {
                            Text(
                                text = placeholder,
                                color = MaterialTheme.colorScheme.extended.textPlaceholder,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        innerBox()
                    }

                    Icon(
                        imageVector = if (isPasswordVisible) {
                            vectorResource(Res.drawable.eye_off_icon)
                        } else {
                            vectorResource(Res.drawable.eye_icon)
                        },
                        contentDescription = if (isPasswordVisible) {
                            stringResource(Res.string.hide_password)
                        } else {
                            stringResource(Res.string.show_password)
                        },
                        tint = MaterialTheme.colorScheme.extended.textDisabled,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(
                                    bounded = false,
                                    radius = 24.dp
                                ),
                                onClick = onToggleVisibilityClick
                            )
                    )
                }
            }
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpPasswordTextFieldEmptyPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState(),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            modifier = Modifier
                .width(300.dp),
            placeholder = "Password",
            title = "Password",
            supportingText = "Use 9+ characters, at least one digit and one uppercase letter",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpPasswordTextFieldFilledPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState("password123"),
            isPasswordVisible = false,
            onToggleVisibilityClick = {},
            modifier = Modifier
                .width(300.dp),
            placeholder = "Password",
            title = "Password",
            supportingText = "Use 9+ characters, at least one digit and one uppercase letter",
        )
    }
}

@Composable
@Preview(
    showBackground = true
)
fun ChirpPasswordTextFieldErrorPreview() {
    ChirpTheme {
        ChirpPasswordTextField(
            state = rememberTextFieldState("password123"),
            isPasswordVisible = true,
            onToggleVisibilityClick = {},
            modifier = Modifier
                .width(300.dp),
            placeholder = "Password",
            title = "Password",
            supportingText = "Doesn't contain an uppercase character",
            isError = true,
        )
    }
}