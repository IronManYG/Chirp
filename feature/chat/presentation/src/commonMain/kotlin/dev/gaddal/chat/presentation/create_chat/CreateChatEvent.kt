package dev.gaddal.chat.presentation.create_chat

import dev.gaddal.chat.domain.models.Chat

/**
 * Represents events related to the creation of a chat.
 *
 * This sealed interface defines the events that can occur during the process
 * of creating a new chat, such as successfully creating a chat.
 */
sealed interface CreateChatEvent {
    data class OnChatCreated(val chat: Chat) : CreateChatEvent
}