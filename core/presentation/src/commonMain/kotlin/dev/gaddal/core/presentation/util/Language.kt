package dev.gaddal.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * A CompositionLocal holding the current app language code (e.g., "en", "ar").
 *
 * Use this to trigger recomposition when the language changes. UI text retrieved via
 * Compose Multiplatform resources (stringResource) will resolve using the current
 * default Locale on each platform. Update the default Locale via
 * `LanguageManager.setLanguage(code)` and update the provided value to force
 * recomposition.
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
 * A platform-specific utility class responsible for applying locale settings such as language
 * and potentially regional or layout-related adjustments.
 *
 * This class is expected to enable locale changes dynamically within an application,
 * ensuring that the user interface reflects the selected language.
 */
expect class LocaleApplier {
    /**
     * Applies the specified language code to the application's locale settings.
     *
     * This method is typically used to change the language of the application at runtime
     * to ensure that the platform locale aligns with the given language code.
     *
     * @param languageCode The language code to be applied (e.g., "en", "ar").
     */
    fun apply(languageCode: String)
}
