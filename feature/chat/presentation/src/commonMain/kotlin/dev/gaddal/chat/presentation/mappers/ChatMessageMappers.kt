package dev.gaddal.chat.presentation.mappers

import dev.gaddal.chat.domain.models.MessageWithSender
import dev.gaddal.chat.presentation.model.MessageUi
import dev.gaddal.chat.presentation.util.DateUtils

/**
 * Converts a [MessageWithSender] domain model into a [MessageUi] UI model for display in the chat interface.
 *
 * The function determines whether a message was sent by the local user or another user based on the
 * provided local user ID, and transforms the message content, delivery status, sender details,
 * and the formatted sent time into an appropriate [MessageUi] subclass.
 *
 * @param localUserId The unique identifier of the local user, used to distinguish between local user's messages
 * and messages from other users.
 * @return A [MessageUi.LocalUserMessage] instance if the message was sent by the local user, or a
 * [MessageUi.OtherUserMessage] instance if the message was sent by another user.
 */
fun MessageWithSender.toUi(
    localUserId: String,
): MessageUi {
    val isFromLocalUser = this.sender.userId == localUserId
    return if (isFromLocalUser) {
        MessageUi.LocalUserMessage(
            id = message.id,
            content = message.content,
            deliveryStatus = message.deliveryStatus,
            isMenuOpen = false,
            formattedSentTime = DateUtils.formatMessageTime(instant = message.createdAt)
        )
    } else {
        MessageUi.OtherUserMessage(
            id = message.id,
            content = message.content,
            formattedSentTime = DateUtils.formatMessageTime(instant = message.createdAt),
            sender = sender.toUi()
        )
    }
}