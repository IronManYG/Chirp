package dev.gaddal.chat.data.di

import dev.gaddal.chat.data.chat.KtorChatParticipantService
import dev.gaddal.chat.domain.chat.ChatParticipantService
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Koin module definition for providing the chat data layer dependencies in the application.
 *
 * It serves as a central point for defining dependencies required by the data components of the application.
 */
val chatDataModule = module {
    singleOf(::KtorChatParticipantService) bind ChatParticipantService::class
}