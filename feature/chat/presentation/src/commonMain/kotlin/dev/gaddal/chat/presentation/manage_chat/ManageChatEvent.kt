package dev.gaddal.chat.presentation.manage_chat

/**
 * Represents events related to managing a chat.
 *
 * Implementations of this interface are used to signal specific actions or occurrences
 * during the management of a chat.
 */
sealed interface ManageChatEvent {
    data object OnMembersAdded : ManageChatEvent
}