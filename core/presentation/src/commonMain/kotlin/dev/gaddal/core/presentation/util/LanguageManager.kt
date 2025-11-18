package dev.gaddal.core.presentation.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Manages app language state and provides validation for language switching.
 *
 * This centralized manager ensures that:
 * - Only supported languages are applied
 * - Language changes are validated before application
 * - A fallback mechanism exists for unsupported codes
 * - State management is unified across the app
 *
 * Usage:
 * ```
 * val manager = LanguageManager(
 *     supportedLanguages = setOf("en", "ar"),
 *     defaultLanguage = "en"
 * )
 *
 * // Change language with validation
 * manager.setLanguage("ar") // returns true if successful
 *
 * // Access current language
 * val currentLang = manager.currentLanguage
 * ```
 */
class LanguageManager(
    private val supportedLanguages: Set<String>,
    private val defaultLanguage: String,
    private val localeApplier: LocaleApplier,
    initialLanguage: String? = null
) {
    init {
        require(supportedLanguages.isNotEmpty()) {
            "LanguageManager must have at least one supported language"
        }
        require(defaultLanguage in supportedLanguages) {
            "Default language '$defaultLanguage' must be in supported languages: $supportedLanguages"
        }
    }

    /**
     * Current app language code. Observable via Compose.
     */
    var currentLanguage: String by mutableStateOf(
        validateAndNormalize(initialLanguage) ?: defaultLanguage
    )
        private set

    init {
        // Ensure platform locale matches the initial/current language at startup.
        // Without this, the app might use the device's default locale (e.g., Arabic)
        // even when our app's default is English until the user toggles languages.
        localeApplier.apply(currentLanguage)
    }

    /**
     * Changes the app language if the provided code is supported.
     *
     * @param languageCode The language code to switch to (e.g., "en", "ar")
     * @return true if the language was changed successfully, false if invalid
     */
    fun setLanguage(languageCode: String): Boolean {
        val normalized = validateAndNormalize(languageCode)
        if (normalized == null) {
            // Invalid or unsupported language
            return false
        }

        if (currentLanguage == normalized) {
            // Already on this language
            return true
        }

        // Update platform locale via the injected applier using the NEW language
        localeApplier.apply(normalized)

        // Update state (triggers recomposition)
        currentLanguage = normalized

        return true
    }

    /**
     * Checks if a language code is supported.
     */
    fun isSupported(languageCode: String): Boolean {
        return validateAndNormalize(languageCode) != null
    }

    /**
     * Returns the list of all supported language codes.
     */
    fun getSupportedLanguages(): Set<String> = supportedLanguages.toSet()

    /**
     * Validates and normalizes a language code.
     *
     * Normalizes the code to lowercase and extracts the primary language subtag
     * (e.g., "EN" -> "en", "ar-EG" -> "ar").
     *
     * @return normalized code if supported, null otherwise
     */
    private fun validateAndNormalize(languageCode: String?): String? {
        if (languageCode.isNullOrBlank()) return null

        val normalized = languageCode.trim().lowercase().substringBefore('-')
        return if (normalized in supportedLanguages) normalized else null
    }

    /**
     * Returns whether the current language is RTL.
     */
    fun isCurrentLanguageRtl(): Boolean {
        return isRtlLanguage(currentLanguage)
    }
}
