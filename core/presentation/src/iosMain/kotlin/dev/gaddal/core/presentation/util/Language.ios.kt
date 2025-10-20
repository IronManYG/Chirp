package dev.gaddal.core.presentation.util

import platform.Foundation.NSUserDefaults

actual fun changeLanguage(languageCode: String) {
    // Update the preferred languages so the first one is our selected code.
    // Compose resources consult the default locale; on iOS this helps next resource lookups.
    NSUserDefaults.standardUserDefaults.setObject(listOf(languageCode), forKey = "AppleLanguages")
}
