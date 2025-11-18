package dev.gaddal.chat.data.mappers

import dev.gaddal.chat.data.dto.ChatMessageDto
import dev.gaddal.chat.database.entities.ChatMessageEntity
import dev.gaddal.chat.database.view.LastMessageView
import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.chat.domain.models.ChatMessageDeliveryStatus
import kotlin.time.Instant

/**
 * Converts the current `ChatMessageDto` instance to its corresponding domain model `ChatMessage`.
 *
 * Maps the properties of the `ChatMessageDto`, including its identifier, chat association,
 * content, creation timestamp, and sender information, to the equivalent properties of `ChatMessage`.
 *
 * @return The domain model `ChatMessage` instance derived from the current `ChatMessageDto`.
 */
fun ChatMessageDto.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = Instant.parse(createdAt),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT
    )
}

/**
 * Converts the current `ChatMessageEntity` instance to its corresponding domain model `ChatMessage`.
 *
 * This function maps the database entity fields, such as message content, sender, timestamp, and
 * delivery status, to the equivalent properties in the domain model. The `createdAt` field in
 * the domain model is derived from the `timestamp` field, and the delivery status is set to
 * `SENT` by default.
 *
 * @return The domain model `ChatMessage` instance derived from the current `ChatMessageEntity`.
 */
fun ChatMessageEntity.toDomain(): ChatMessage {
    return ChatMessage(
        id = chatId,
        chatId = chatId,
        content = content,
        createdAt = Instant.fromEpochMilliseconds(timestamp),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.SENT
    )
}

/**
 * Converts the `LastMessageView` database view model to its corresponding `ChatMessage` domain model.
 *
 * This function maps the properties of the `LastMessageView`, such as the message ID, chat ID,
 * content, timestamp, sender ID, and delivery status, to the equivalent properties in the `ChatMessage`
 * domain model.
 *
 * @return The domain model `ChatMessage` instance derived from the `LastMessageView`.
 */
fun LastMessageView.toDomain(): ChatMessage {
    return ChatMessage(
        id = messageId,
        chatId = chatId,
        content = content,
        createdAt = Instant.fromEpochMilliseconds(timestamp),
        senderId = senderId,
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(this.deliveryStatus)
    )
}

/**
 * Converts a `ChatMessage` instance into its corresponding database entity `ChatMessageEntity`.
 *
 * This function maps the properties of the `ChatMessage` domain model, such as the unique
 * identifiers, content, timestamps, and delivery status, into a structure suitable for
 * persistence in the database.
 *
 * @return The `ChatMessageEntity` database representation derived from the `ChatMessage` instance.
 */
fun ChatMessage.toEntity(): ChatMessageEntity {
    return ChatMessageEntity(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name
    )
}

/**
 * Converts an instance of `ChatMessage` to a `LastMessageView` representation.
 *
 * This method extracts relevant details from a `ChatMessage` entity, such as the message ID,
 * chat ID, sender ID, content, timestamp (in milliseconds), and delivery status, and maps them
 * to a `LastMessageView` object for use in database views or UI representations.
 *
 * @return A `LastMessageView` object containing the key attributes of the last message.
 */
fun ChatMessage.toLastMessageView(): LastMessageView {
    return LastMessageView(
        messageId = id,
        chatId = chatId,
        senderId = senderId,
        content = content,
        timestamp = createdAt.toEpochMilliseconds(),
        deliveryStatus = deliveryStatus.name
    )
}