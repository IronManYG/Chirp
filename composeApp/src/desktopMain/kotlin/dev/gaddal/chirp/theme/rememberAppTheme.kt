package dev.gaddal.chirp.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.jthemedetecor.OsThemeDetector
import dev.gaddal.core.domain.preferences.ThemePreference
import java.util.function.Consumer

/**
 * Represents the application theme used to style the user interface.
 *
 * This enum provides two theme options:
 * - `LIGHT`: A theme with light colors.
 * - `DARK`: A theme with dark colors.
 *
 * The `AppTheme` is used throughout the application to apply a consistent visual style
 * by switching between light and dark themes based on user preferences or system settings.
 */
enum class AppTheme {
    LIGHT, DARK
}

/**
 * Determines and remembers the application's active theme based on the user's preferences
 * and system settings. This function observes the system's theme (if supported) and dynamically
 * adjusts the application theme to match the user's preference.
 *
 * @param themePreferenceFromAppSettings The user's theme preference, defined by `ThemePreference`.
 * It can be light, dark, or system-based.
 * @return The active application theme as `AppTheme` (either `AppTheme.LIGHT` or `AppTheme.DARK`),
 * computed by applying the user's preference or adapting to the system's current theme setting.
 */
@Composable
fun rememberAppTheme(
    themePreferenceFromAppSettings: ThemePreference
): AppTheme {
    var isSystemThemeDark by remember {
        if (OsThemeDetector.isSupported()) {
            mutableStateOf(OsThemeDetector.getDetector().isDark)
        } else {
            val isSettingsPreferenceDark = themePreferenceFromAppSettings == ThemePreference.DARK
            mutableStateOf(isSettingsPreferenceDark)
        }
    }

    DisposableEffect(Unit) {
        var listener: Consumer<Boolean>? = null
        if (OsThemeDetector.isSupported()) {
            listener = Consumer<Boolean> { dark -> isSystemThemeDark = dark }
            OsThemeDetector.getDetector().registerListener(listener)
        }

        onDispose {
            OsThemeDetector.getDetector().removeListener(listener)
        }
    }

    val isDarkTheme = when (themePreferenceFromAppSettings) {
        ThemePreference.LIGHT -> false
        ThemePreference.DARK -> true
        ThemePreference.SYSTEM -> isSystemThemeDark
    }

    return if (isDarkTheme) AppTheme.DARK else AppTheme.LIGHT
}