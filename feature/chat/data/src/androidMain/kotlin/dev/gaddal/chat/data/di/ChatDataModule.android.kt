package dev.gaddal.chat.data.di

import dev.gaddal.chat.data.lifecycle.AppLifecycleObserver
import dev.gaddal.chat.data.network.ConnectivityObserver
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
 * - `ConnectivityObserver`: A class that monitors network connectivity status.
 *
 * This module ensures that platform-specific configurations required for database operations, lifecycle monitoring,
 * and network connectivity tracking are properly set up for the Android platform.
 *
 * Dependencies:
 * - `DatabaseFactory`: Initializes the `ChirpChatDatabase` with Android application context.
 * - `AppLifecycleObserver`: Tracks application foreground/background state using Android lifecycle components.
 * - `ConnectivityObserver`: Monitors network connectivity status using Android connectivity manager.
 *
 * Usage Context:
 * - This Koin module should be included in an application's dependency injection setup,
 *   allowing the database factory, lifecycle services, and connectivity observer to be injected wherever needed.
 */
actual val platformChatDataModule = module {
    single { DatabaseFactory(androidContext()) }
    singleOf(::AppLifecycleObserver)
    singleOf(::ConnectivityObserver)
}