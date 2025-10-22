package dev.gaddal.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.CompositionLocalProvider

/**
 * A CompositionLocal holding the current app language code (e.g., "en", "ar").
 *
 * Use this to trigger recomposition when the language changes. UI text retrieved via
 * Compose Multiplatform resources (stringResource) will resolve using the current
 * default Locale on each platform. Update the default Locale via [changeLanguage]
 * and update the provided value to force recomposition.
 */
val LocalAppLanguage: ProvidableCompositionLocal<String> = staticCompositionLocalOf { "en" }

/**
 * Convenience wrapper to provide the current app language into the composition.
 */
@Composable
fun ProvideAppLanguage(languageCode: String, content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAppLanguage provides languageCode) {
        content()
    }
}

/**
 * Change the app language at runtime.
 *
 * This function must update the platform's effective default locale/language so that calls to
 * stringResource(...) pick up the new language. After invoking this, also update any state
 * that reads [LocalAppLanguage] to trigger recomposition.
 *
 * Example:
 *  setLanguageCode(newCode)
 *  changeLanguage(newCode)
 */
expect fun changeLanguage(languageCode: String)
