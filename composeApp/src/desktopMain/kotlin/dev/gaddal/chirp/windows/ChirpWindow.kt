package dev.gaddal.chirp.windows

import androidx.compose.runtime.Composable
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyShortcut
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.MenuBar
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import chirp.composeapp.generated.resources.Res
import chirp.composeapp.generated.resources.file
import chirp.composeapp.generated.resources.logo
import chirp.composeapp.generated.resources.new_window
import dev.gaddal.chirp.App
import dev.gaddal.chirp.theme.AppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Displays the main Chirp application window.
 *
 * The `ChirpWindow` composable function represents the primary user interface window of the application.
 * It supports dynamic theming, window-specific actions, and focus state management.
 *
 * @param appTheme The theme of the application, represented as an `AppTheme` (e.g., light or dark).
 * @param onCloseRequest A callback function invoked when the user requests to close the window.
 * @param onAddWindowClick A callback function invoked when the user selects the "Add New Window" option.
 * @param onFocusChanged A callback function invoked with the focus state of the window as a parameter.
 */
@Composable
fun ChirpWindow(
    appTheme: AppTheme,
    onCloseRequest: () -> Unit,
    onAddWindowClick: () -> Unit,
    onFocusChanged: (Boolean) -> Unit
) {
    val windowState = rememberWindowState(
        width = 1200.dp,
        height = 800.dp
    )
    Window(
        onCloseRequest = onCloseRequest,
        state = windowState,
        title = "Chirp",
        icon = painterResource(Res.drawable.logo)
    ) {
        MenuBar {
            Menu(
                text = stringResource(Res.string.file),
                mnemonic = 'F'
            ) {
                Item(
                    text = stringResource(Res.string.new_window),
                    mnemonic = 'N',
                    shortcut = KeyShortcut(
                        key = Key.N,
                        ctrl = true,
                        shift = true
                    ),
                    onClick = onAddWindowClick
                )
            }
        }

        App(
            isDarkTheme = appTheme == AppTheme.DARK
        )
    }
}