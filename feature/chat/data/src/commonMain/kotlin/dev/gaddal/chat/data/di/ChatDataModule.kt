package dev.gaddal.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import dev.gaddal.chat.data.chat.KtorChatParticipantService
import dev.gaddal.chat.data.chat.KtorChatService
import dev.gaddal.chat.database.DatabaseFactory
import dev.gaddal.chat.domain.chat.ChatParticipantService
import dev.gaddal.chat.domain.chat.ChatService
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


/**
 * A platform-specific Koin module definition for providing dependencies
 * related to the chat feature. This module is expected to be implemented
 * individually for each platform using the 'expect/actual' mechanism.
 *
 * Designed to encapsulate platform-specific configurations or dependencies
 * required for the chat functionality, while maintaining a unified interface
 * across all supported platforms.
 */
expect val platformChatDataModule: Module

/**
 * Koin module definition for providing the chat data layer dependencies in the application.
 *
 * This module serves as the dependency injection configuration point for chat data layer components:
 * - Includes platform-specific chat data dependencies
 * - Provides Ktor-based implementations for chat and participant services
 * - Configures and builds the SQLite database using BundledSQLiteDriver
 */
val chatDataModule = module {
    includes(platformChatDataModule)

    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
    singleOf(::KtorChatService) bind ChatService::class
    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}