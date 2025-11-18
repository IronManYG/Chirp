package dev.gaddal.chat.data.di

import dev.gaddal.chat.data.lifecycle.AppLifecycleObserver
import dev.gaddal.chat.database.DatabaseFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * Provides a Koin module that defines platform-specific dependencies for managing chat data storage
 * in the Android environment.
 *
 * The module configures singleton instances for database access and lifecycle monitoring:
 * - `DatabaseFactory`: A factory class for initializing the `ChirpChatDatabase` using Room database builder.
 * - `AppLifecycleObserver`: A class that monitors application lifecycle state (foreground/background).
 *
 * This module ensures that platform-specific configurations required for database operations and
 * lifecycle monitoring are properly set up for the Android platform.
 *
 * Dependencies:
 * - `DatabaseFactory`: Initializes the `ChirpChatDatabase` with Android application context.
 * - `AppLifecycleObserver`: Tracks application foreground/background state using Android lifecycle components.
 *
 * Usage Context:
 * - This Koin module should be included in an application's dependency injection setup,
 *   allowing the database factory and lifecycle services to be injected wherever needed.
 */
actual val platformChatDataModule = module {
    single { DatabaseFactory(androidContext()) }
    singleOf(::AppLifecycleObserver)
}