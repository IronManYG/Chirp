package dev.gaddal.chat.data.di

import dev.gaddal.chat.database.DatabaseFactory
import org.koin.dsl.module

/**
 * A module that provides platform-specific dependencies for handling chat-related database
 * operations on the iOS platform.
 *
 * This module binds the `DatabaseFactory` class, which is responsible for configuring and
 * initializing the `ChirpChatDatabase`, into the dependency injection graph. The `DatabaseFactory`
 * ensures the database is set up in the required iOS environment, managing the file location
 * and initialization logic.
 *
 * Key responsibilities:
 * - Ensures proper configuration of the database for iOS devices.
 * - Provides a singleton instance of `DatabaseFactory` to manage database creation.
 *
 * Intended usage:
 * This module is part of the dependency injection setup, enabling seamless integration
 * of the database layer in the application by providing platform-specific support.
 */
actual val platformChatDataModule = module {
    single { DatabaseFactory() }
}