package dev.gaddal.core.presentation.util

import java.util.Locale

actual fun changeLanguage(languageCode: String) {
    // Update the default Locale for the process. Compose resources consult Locale.getDefault().
    val locale = Locale.forLanguageTag(languageCode)
    Locale.setDefault(locale)
}
