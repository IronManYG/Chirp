package dev.gaddal.core.presentation.util

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/**
 * A platform-specific implementation of the `LocaleApplier` class responsible for applying
 * locale settings such as language and layout direction adjustments to the application.
 *
 * This implementation manages runtime language changes by updating the application-level
 * locale settings and ensuring synchronization with the platform's locale APIs.
 *
 * The class accommodates per-app locale settings for Android 33+ (Tiramisu) and provides
 * backward compatibility for older Android versions using `AppCompatDelegate`.
 *
 * @constructor Creates an instance of `LocaleApplier` for the given application context.
 * @param context The application context used to configure and apply locale settings.
 */
actual class LocaleApplier(
    private val context: Context
) {
    /**
     * Applies the specified language code to the application's locale settings.
     *
     * This method ensures that the application's language and layout direction
     * align with the specified language. It adjusts the default `Locale` for the JVM
     * and leverages platform-specific APIs to update the application's locale
     * configuration based on the device's API level.
     *
     * @param languageCode The language code to be applied (e.g., "en", "ar").
     */
    actual fun apply(languageCode: String) {
        // Update the default Locale for the process. Compose resources consult Locale.getDefault().
        val locale = Locale.forLanguageTag(languageCode)
        // Keep JVM/Process default aligned for Compose resource resolution
        Locale.setDefault(locale)

        if (Build.VERSION.SDK_INT >= 33) {
            // Use Android per-app locales on Tiramisu+
            val lm = context.getSystemService(LocaleManager::class.java)
            lm?.applicationLocales = LocaleList.forLanguageTags(languageCode)
        } else {
            // Backport via AppCompatDelegate for older devices
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(languageCode)
            )
        }
    }
}