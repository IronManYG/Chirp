package dev.gaddal.core.presentation.di

import dev.gaddal.core.presentation.util.LocaleApplier
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Represents the dependency injection module for the core presentation layer of the platform.
 *
 * This module defines and provides a singleton instance of `LocaleApplier` which is responsible
 * for managing locale settings within the application. The `LocaleApplier` ensures proper
 * runtime localization configuration, including adjustments for language and layout direction,
 * based on the application's requirements.
 *
 * The `platformCorePresentationModule` registers the `LocaleApplier` implementation in the DI
 * container, utilizing the provided application context to support locale-related operations
 * tailored to the Android platform.
 */
actual val platformCorePresentationModule = module {
    single { LocaleApplier(androidContext()) }
}