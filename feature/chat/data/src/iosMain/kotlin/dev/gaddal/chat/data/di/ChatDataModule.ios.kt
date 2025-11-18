package dev.gaddal.chat.data.di

import dev.gaddal.chat.data.lifecycle.AppLifecycleObserver
import dev.gaddal.chat.database.DatabaseFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

/**
 * A module that provides platform-specific dependencies for handling chat-related database
 * operations on the iOS platform.
 *
 * This module configures singleton instances for database access and lifecycle monitoring:
 * - `DatabaseFactory`: A factory class for initializing the `ChirpChatDatabase` using Room database builder.
 * - `AppLifecycleObserver`: A class that monitors application lifecycle state (foreground/background).
 *
 * This module ensures that platform-specific configurations required for database operations and
 * lifecycle monitoring are properly set up for the iOS platform.
 *
 * Dependencies:
 * - `DatabaseFactory`: Initializes the `ChirpChatDatabase` with iOS document directory path.
 * - `AppLifecycleObserver`: Tracks application foreground/background state using iOS notifications.
 *
 * Usage Context:
 * - This Koin module should be included in an application's dependency injection setup,
 *   allowing the database factory and lifecycle services to be injected wherever needed.
 */
actual val platformChatDataModule = module {
    single { DatabaseFactory() }
    singleOf(::AppLifecycleObserver)
}