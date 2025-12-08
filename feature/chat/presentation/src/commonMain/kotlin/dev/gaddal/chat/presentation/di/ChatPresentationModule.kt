package dev.gaddal.chat.presentation.di

import dev.gaddal.chat.presentation.chat_detail.ChatDetailViewModel
import dev.gaddal.chat.presentation.chat_list.ChatListViewModel
import dev.gaddal.chat.presentation.chat_list_detail.ChatListDetailViewModel
import dev.gaddal.chat.presentation.create_chat.CreateChatViewModel
import dev.gaddal.chat.presentation.manage_chat.ManageChatViewModel
import dev.gaddal.chat.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Dependency injection module that provides presentation-layer dependencies for chat-related screens.
 *
 * The ViewModels are registered using the dependency injection framework, making them available
 * for use within the application wherever needed.
 */
val chatPresentationModule = module {
    viewModelOf(::ChatListViewModel)
    viewModelOf(::ChatListDetailViewModel)
    viewModelOf(::CreateChatViewModel)
    viewModelOf(::ChatDetailViewModel)
    viewModelOf(::ManageChatViewModel)
    viewModelOf(::ProfileViewModel)
}