package dev.gaddal.chirp.di

import dev.gaddal.chirp.MainViewModel
import dev.gaddal.core.presentation.util.LanguageManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Defines the dependency injection module for the application.
 *
 * This module provides the following configurations:
 *
 * - Creates a single instance of `LanguageManager` throughout the app's lifecycle, which:
 *   - Manages supported languages and handles language changes.
 *   - Validates language codes and applies the appropriate locale via a `LocaleApplier`.
 *   - Initializes the default language to "en" unless overridden during startup by the `MainViewModel`.
 *
 * - Registers the `MainViewModel` to be used within the app's view layer.
 *
 * - Provides a singleton `CoroutineScope` with a `SupervisorJob` and `Dispatchers.Default` for
 *   handling background operations throughout the app.
 */
val appModule = module {
    // Single language manager shared across the app
    single {
        LanguageManager(
            supportedLanguages = setOf("en", "ar"),
            defaultLanguage = "en",
            localeApplier = get(),
            initialLanguage = null // Will be overridden on startup by MainViewModel from SettingsStorage
        )
    }

    // MainViewModel
    viewModelOf(::MainViewModel)
    single {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
}