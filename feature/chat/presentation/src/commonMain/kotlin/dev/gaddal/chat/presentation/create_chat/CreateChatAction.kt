package dev.gaddal.chat.presentation.create_chat

/**
 * Represents user actions that can be triggered within the Create Chat feature.
 *
 * This sealed interface is used to define different actions and user interactions
 * that can be performed while managing the process of creating a new chat.
 */
sealed interface CreateChatAction {
    data object OnAddClick : CreateChatAction
    data object OnDismissDialog : CreateChatAction
    data object OnCreateChatClick : CreateChatAction
}