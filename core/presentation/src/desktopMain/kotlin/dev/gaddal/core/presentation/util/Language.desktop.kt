package dev.gaddal.core.presentation.util

import java.util.Locale

/**
 * A platform-specific implementation of the `LocaleApplier` class responsible for applying
 * locale settings such as language and layout direction adjustments to the application.
 *
 * This implementation manages runtime language changes by updating the application-level
 * locale settings using the JVM's Locale API.
 *
 * @constructor Creates an instance of `LocaleApplier` for desktop platforms.
 */
actual class LocaleApplier {
    /**
     * Applies the specified language code to the application's locale settings.
     *
     * This method ensures that the application's language settings are updated
     * by setting the default Locale for the JVM process. This allows Compose
     * resources to correctly access the updated locale through Locale.getDefault().
     *
     * @param languageCode The language code to be applied (e.g., "en", "ar").
     */
    actual fun apply(languageCode: String) {
        // Update the default Locale for the process. Compose resources consult Locale.getDefault().
        val locale = Locale.forLanguageTag(languageCode)
        // Keep JVM/Process default aligned for Compose resource resolution
        Locale.setDefault(locale)
    }
}
