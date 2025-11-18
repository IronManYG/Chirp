package dev.gaddal.chat.data.mappers

import dev.gaddal.chat.data.dto.ChatDto
import dev.gaddal.chat.database.entities.ChatEntity
import dev.gaddal.chat.database.entities.ChatInfoEntity
import dev.gaddal.chat.database.entities.ChatWithParticipants
import dev.gaddal.chat.database.entities.MessageWithSender
import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.chat.domain.models.ChatInfo
import dev.gaddal.chat.domain.models.ChatMessage
import dev.gaddal.chat.domain.models.ChatMessageDeliveryStatus
import dev.gaddal.chat.domain.models.ChatParticipant
import kotlin.time.Instant

typealias DataMessageWithSender = MessageWithSender
typealias DomainMessageWithSender = dev.gaddal.chat.domain.models.MessageWithSender

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
 * Converts a `ChatEntity` instance into its corresponding domain model `Chat`.
 *
 * This method maps the properties of the `ChatEntity`, such as the unique chat identifier and the last
 * activity timestamp, along with additional information about chat participants and the last message,
 * to create a complete representation of a `Chat`.
 *
 * @param participants A list of `ChatParticipant` representing all participants involved in the chat.
 * @param lastMessage The most recent `ChatMessage` in the chat, or null if no messages have been exchanged.
 * @return A `Chat` domain model instance representing the chat with its participants, last activity timestamp,
 *         and the latest message if available.
 */
fun ChatEntity.toDomain(
    participants: List<ChatParticipant>,
    lastMessage: ChatMessage? = null
): Chat {
    return Chat(
        id = chatId,
        participants = participants,
        lastActivityAt = Instant.fromEpochMilliseconds(lastActivityAt),
        lastMessage = lastMessage
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

/**
 * Converts the current `DataMessageWithSender` instance to its corresponding domain model `DomainMessageWithSender`.
 *
 * This method maps the properties of the message and sender to their respective domain models
 * and translates the delivery status into its domain representation.
 *
 * @return The domain model `DomainMessageWithSender` derived from the current `DataMessageWithSender`.
 */
fun DataMessageWithSender.toDomain(): DomainMessageWithSender {
    return DomainMessageWithSender(
        message = message.toDomain(),
        sender = sender.toDomain(),
        deliveryStatus = ChatMessageDeliveryStatus.valueOf(this.message.deliveryStatus)
    )
}

/**
 * Maps the current `ChatInfoEntity` instance to its corresponding domain model `ChatInfo`.
 *
 * This function transforms the `ChatInfoEntity` by converting its embedded `ChatEntity`
 * and related participant and message data to their respective domain model representations.
 *
 * The resulting `ChatInfo` model provides a comprehensive view of the chat, including
 * details about the chat itself, its participants, and associated messages with sender information.
 *
 * @return The `ChatInfo` domain model instance derived from the current `ChatInfoEntity`.
 */
fun ChatInfoEntity.toDomain(): ChatInfo {
    return ChatInfo(
        chat = chat.toDomain(
            participants = this.participants.map { it.toDomain() }
        ),
        messages = messagesWithSenders.map { it.toDomain() }
    )
}