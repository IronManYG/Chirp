package dev.gaddal.chirp

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.TrayState
import chirp.composeapp.generated.resources.Res
import chirp.composeapp.generated.resources.app_theme
import chirp.composeapp.generated.resources.logo
import dev.gaddal.core.domain.preferences.ThemePreference
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Represents the menu for the system tray in the Chirp application.
 *
 * This function defines the behavior and structure of the system tray menu, allowing users
 * to interact with it for setting preferences, specifically the application's theme.
 * The menu is dynamically populated based on available theme preferences and reflects the
 * current theme setting.
 *
 * @param state The current state of the system tray, used to manage its visibility and interactions.
 * @param themePreferenceFromAppSettings The currently selected theme preference as defined
 *        in the application's settings.
 * @param onThemePreferenceClick A callback function invoked when the user selects a theme
 *        preference. It receives the selected `ThemePreference`.
 */
@Composable
fun ApplicationScope.ChirpTrayMenu(
    state: TrayState,
    themePreferenceFromAppSettings: ThemePreference,
    onThemePreferenceClick: (ThemePreference) -> Unit
) {
    Tray(
        icon = painterResource(Res.drawable.logo),
        state = state,
    ) {
        Menu(
            text = stringResource(Res.string.app_theme)
        ) {
            ThemePreference.entries.forEach { themePreference ->
                // Todo: Replace CheckboxItem with RadioButtonItem (not working yet)
                CheckboxItem(
                    text = themePreference.name.lowercase().replaceFirstChar { it.titlecase() },
                    onCheckedChange = {
                        onThemePreferenceClick(themePreference)
                    },
                    checked = themePreferenceFromAppSettings == themePreference
                )
            }
        }
    }
}