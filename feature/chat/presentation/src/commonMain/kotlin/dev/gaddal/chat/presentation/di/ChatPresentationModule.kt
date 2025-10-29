package dev.gaddal.chat.presentation.di

import dev.gaddal.chat.presentation.chat_list.ChatListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatPresentationModule = module {
    viewModelOf(::ChatListViewModel)
}