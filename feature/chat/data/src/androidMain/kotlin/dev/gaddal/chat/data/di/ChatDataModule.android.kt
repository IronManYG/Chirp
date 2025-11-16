package dev.gaddal.chat.data.di

import dev.gaddal.chat.database.DatabaseFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/**
 * Provides a Koin module that defines platform-specific dependencies for managing chat data storage
 * in the Android environment.
 *
 * The module configures a singleton instance of `DatabaseFactory` with the Android application
 * context, enabling the creation and initialization of the `ChirpChatDatabase`. This database
 * facilitates persistent storage and retrieval of chat-related data, including chats, participants,
 * and messages.
 *
 * This module ensures that platform-specific configurations required for database operations,
 * such as accessing the correct file paths, are properly set up for the Android platform.
 *
 * Dependencies:
 * - `DatabaseFactory`: A factory class for initializing the `ChirpChatDatabase` using the Room database builder.
 *
 * Usage Context:
 * - This Koin module is intended to be included in an application's dependency injection setup,
 *   allowing the database factory and related services to be injected wherever needed.
 */
actual val platformChatDataModule = module {
    single { DatabaseFactory(androidContext()) }
}