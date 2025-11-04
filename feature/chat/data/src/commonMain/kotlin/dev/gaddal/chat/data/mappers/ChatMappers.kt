package dev.gaddal.chat.data.mappers

import dev.gaddal.chat.data.dto.ChatDto
import dev.gaddal.chat.domain.models.Chat
import kotlin.time.Instant

/**
 * Converts the current `ChatDto` data transfer object to its corresponding domain model `Chat`.
 *
 * This function maps the properties of the `ChatDto`, including its identifier, participants,
 * last activity timestamp, and last message (if present), to the equivalent properties in the
 * domain model `Chat`.
 *
 * @return The domain model `Chat` instance derived from the current `ChatDto`.
 */
fun ChatDto.toDomain(): Chat {
    return Chat(
        id = id,
        participants = participants.map { it.toDomain() },
        lastActivityAt = Instant.parse(lastActivityAt),
        lastMessage = lastMessage?.toDomain()
    )
}