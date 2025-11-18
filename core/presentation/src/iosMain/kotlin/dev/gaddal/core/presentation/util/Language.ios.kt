package dev.gaddal.core.presentation.util

import platform.Foundation.NSUserDefaults

/**
 * A platform-specific utility class responsible for applying locale settings such as language
 * and potentially other regional adjustments on iOS.
 *
 * This implementation updates the iOS system's preferred languages (AppleLanguages) to reflect
 * the selected language code. This ensures resource lookups and other locale-dependent behaviors
 * align with the specified language.
 */
actual class LocaleApplier {
    /**
     * Applies the specified language code to the application's locale settings.
     *
     * This function updates the platform's language preferences to ensure that
     * the runtime locale matches the provided language code. On iOS, this is achieved
     * by modifying the `AppleLanguages` setting in `NSUserDefaults`, which helps
     * in resolving resources and maintaining consistency in the UI language.
     *
     * @param languageCode The language code to be applied (e.g., "en" for English, "ar" for Arabic).
     */
    actual fun apply(languageCode: String) {
        // Update the preferred languages so the first one is our selected code.
        // Compose resources consult the default locale; on iOS this helps next resource lookups.
        NSUserDefaults.standardUserDefaults.setObject(
            listOf(languageCode),
            forKey = "AppleLanguages"
        )
    }
}