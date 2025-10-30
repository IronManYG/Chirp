package dev.gaddal.chirp.di

import dev.gaddal.chirp.MainViewModel
import dev.gaddal.core.presentation.util.LanguageManager
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module definition for dependency injection in the application.
 */
val appModule = module {
    // Single language manager shared across the app
    singleOf(::provideLanguageManager)

    // MainViewModel
    viewModelOf(::MainViewModel)
}

private fun provideLanguageManager(): LanguageManager = LanguageManager(
    supportedLanguages = setOf("en", "ar"),
    defaultLanguage = "en",
    initialLanguage = null // Will be overridden on startup by MainViewModel from SettingsStorage
)