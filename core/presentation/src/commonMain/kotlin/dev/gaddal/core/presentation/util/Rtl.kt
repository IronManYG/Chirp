package dev.gaddal.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.platform.LocalLayoutDirection

/**
 * Provides both app language and layout direction to the composition tree.
 *
 * This is a convenience wrapper around [ProvideAppLanguage] and [ProvideLayoutDirectionFromLanguage]
 * to simplify setup at the root of the application.
 */
@Composable
fun ProvideMultilingualSupport(
    languageCode: String,
    content: @Composable () -> Unit
) {
    ProvideAppLanguage(languageCode) {
        ProvideLayoutDirectionFromLanguage(languageCode) {
            content()
        }
    }
}

/**
 * Provides [LocalLayoutDirection] based on the given [languageCode].
 *
 * Compose Multiplatform mirrors layouts automatically for RTL locales in many environments
 * by consulting the platform default Locale. This helper makes the intent explicit and
 * guarantees consistent behavior across platforms by mapping known RTL languages to
 * [LayoutDirection.Rtl].
 */
@Composable
fun ProvideLayoutDirectionFromLanguage(
    languageCode: String,
    content: @Composable () -> Unit
) {
    val isRtl = isRtlLanguage(languageCode)
    val direction = if (isRtl) LayoutDirection.Rtl else LayoutDirection.Ltr
    CompositionLocalProvider(LocalLayoutDirection provides direction) {
        content()
    }
}

/**
 * Minimal RTL language detection. Extend this list when adding new RTL locales.
 */
fun isRtlLanguage(code: String): Boolean {
    // Normalize to primary language subtag (e.g., "ar", "ar-EG" -> "ar")
    val primary = code.substringBefore('-').lowercase()
    return primary in setOf(
        "ar", // Arabic
        "fa", // Persian (Farsi)
        "he", // Hebrew
        "ur"  // Urdu
    )
}
