package dev.gaddal.chat.data.dto.websocket

import kotlinx.serialization.Serializable

/**
 * Represents the various types of incoming WebSocket events.
 */
enum class IncomingWebSocketType {
    NEW_MESSAGE,
    MESSAGE_DELETED,
    PROFILE_PICTURE_UPDATED,
    CHAT_PARTICIPANTS_CHANGED
}

/**
 * Represents the base class for various types of incoming WebSocket messages.
 * Defines the common `type` property used to identify the specific subtype of the message.
 *
 * @param type The type of the incoming WebSocket message, represented by the [IncomingWebSocketType] enum.
 */
@Serializable
sealed class IncomingWebSocketDto(
    val type: IncomingWebSocketType
) {

    /**
     * Represents the data transfer object for a new message event received through a WebSocket connection.
     * This class contains information about a newly sent or received message in a chat.
     *
     * @property id The unique identifier for the message.
     * @property chatId The unique identifier for the chat to which the message belongs.
     * @property content The actual content of the message.
     * @property senderId The unique identifier of the sender of the message.
     * @property createdAt The timestamp indicating when the message was created.
     */
    @Serializable
    data class NewMessageDto(
        val id: String,
        val chatId: String,
        val content: String,
        val senderId: String,
        val createdAt: String
    ) : IncomingWebSocketDto(IncomingWebSocketType.NEW_MESSAGE)

    /**
     * Data transfer object representing information about a deleted message event
     * received from the WebSocket connection. This class extends the
     * IncomingWebSocketDto base class and specifies the type as MESSAGE_DELETED.
     *
     * @property messageId The unique identifier of the deleted message.
     * @property chatId The identifier of the chat where the message was deleted.
     */
    @Serializable
    data class MessageDeletedDto(
        val messageId: String,
        val chatId: String
    ) : IncomingWebSocketDto(IncomingWebSocketType.MESSAGE_DELETED)

    /**
     * Represents an event received via WebSocket indicating that a user's profile picture has been updated.
     *
     * @constructor Creates an instance of ProfilePictureUpdated.
     * @property userId The unique identifier of the user whose profile picture has been updated.
     * @property newUrl The URL of the updated profile picture. May be null if the profile picture is removed.
     */
    @Serializable
    data class ProfilePictureUpdated(
        val userId: String,
        val newUrl: String?
    ) : IncomingWebSocketDto(IncomingWebSocketType.PROFILE_PICTURE_UPDATED)

    /**
     * Represents a data transfer object for indicating that the participants list of a chat has changed.
     *
     * This DTO is used in scenarios where the backend notifies the client about changes in the
     * participants of a particular chat through a WebSocket message.
     *
     * @property chatId The unique identifier of the chat whose participants have changed.
     */
    @Serializable
    data class ChatParticipantsChangedDto(
        val chatId: String
    ) : IncomingWebSocketDto(IncomingWebSocketType.CHAT_PARTICIPANTS_CHANGED)
}