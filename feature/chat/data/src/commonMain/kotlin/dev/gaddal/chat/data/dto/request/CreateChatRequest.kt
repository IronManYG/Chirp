package dev.gaddal.chat.data.dto.request

import kotlinx.serialization.Serializable

/**
 * Represents a request data structure for creating a new chat session.
 *
 * This data class encapsulates the necessary information required to initiate a new
 * chat, primarily consisting of a list of user IDs representing the participants
 * (excluding the initiating user, which is handled automatically in the backend).
 *
 * @property otherUserIds A list of unique user IDs identifying the participants to
 * include in the created chat. This does not include the ID of the user initiating
 * the request.
 */
@Serializable
data class CreateChatRequest(
    val otherUserIds: List<String>
)