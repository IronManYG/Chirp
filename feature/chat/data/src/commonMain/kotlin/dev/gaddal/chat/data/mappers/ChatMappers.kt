package dev.gaddal.chat.data.mappers

import dev.gaddal.chat.data.dto.ChatDto
import dev.gaddal.chat.database.entities.ChatEntity
import dev.gaddal.chat.database.entities.ChatWithParticipants
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

/**
 * Converts an instance of `ChatWithParticipants` to its corresponding domain model `Chat`.
 *
 * This method transforms the composite data structure, which includes a chat entity, its associated
 * participants, and the last message, into a fully constructed `Chat` domain model.
 *
 * @return A new `Chat` instance representing the domain model equivalent of the `ChatWithParticipants` data structure.
 */
fun ChatWithParticipants.toDomain(): Chat {
    return Chat(
        id = chat.chatId,
        participants = participants.map { it.toDomain() },
        lastActivityAt = Instant.fromEpochMilliseconds(chat.lastActivityAt),
        lastMessage = lastMessage?.toDomain()
    )
}

/**
 * Converts the `Chat` domain model to its corresponding `ChatEntity` for persistence in the database.
 *
 * This method transforms the properties of the domain model, such as the unique identifier
 * and the last activity timestamp, into the structure required by the database entity.
 *
 * @return The `ChatEntity` database representation derived from the current `Chat` instance.
 */
fun Chat.toEntity(): ChatEntity {
    return ChatEntity(
        chatId = id,
        lastActivityAt = lastActivityAt.toEpochMilliseconds()
    )
}