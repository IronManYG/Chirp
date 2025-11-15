package dev.gaddal.chat.presentation.mappers

import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.chat.presentation.model.ChatUi

/**
 * Maps a [Chat] domain model to its UI representation [ChatUi].
 *
 * This function converts a chat containing participants and messages into a UI model,
 * by identifying the local participant using the provided local user ID, and transforming
 * other participants and message-related information to a structure suitable for rendering
 * in the UI layer.
 *
 * @param localParticipantId The unique identifier of the local user in the chat.
 * @return A [ChatUi] instance containing the UI representation of the chat with its participants
 * and the last message details.
 */
fun Chat.toUi(localParticipantId: String): ChatUi {
    val (local, other) = participants.partition { it.userId == localParticipantId }
    return ChatUi(
        id = id,
        localParticipant = local.first().toUi(),
        otherParticipants = other.map { it.toUi() },
        lastMessage = lastMessage,
        lastMessageSenderUsername = participants
            .find { it.userId == lastMessage?.senderId }
            ?.username
    )
}