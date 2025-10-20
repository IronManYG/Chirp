package dev.gaddal.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

/**
 * Composable function that applies a consistent theme to the content based on the selected color scheme
 * (dark or light) and typography. It uses Material 3 styling conventions.
 *
 * @param darkTheme A boolean indicating whether the dark theme should be applied. Defaults to
 * `isSystemInDarkTheme()` to respect the system's theme setting.
 * @param content A composable lambda that specifies the content to be styled with the defined theme.
 */
@Composable
fun ChirpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if(darkTheme) DarkColorScheme else LightColorScheme
    val extendedScheme = if(darkTheme) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalExtendedColors provides extendedScheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}