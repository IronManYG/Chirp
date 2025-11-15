package dev.gaddal.chat.data.mappers

import dev.gaddal.chat.data.dto.ChatMessageDto
import dev.gaddal.chat.domain.models.ChatMessage
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
        senderId = senderId
    )
}