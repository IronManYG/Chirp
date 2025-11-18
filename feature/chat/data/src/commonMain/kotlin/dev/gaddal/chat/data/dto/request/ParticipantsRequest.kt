package dev.gaddal.chat.data.dto.request

import kotlinx.serialization.Serializable

/**
 * Represents a request structure for fetching participants in a chat.
 *
 * This data class is used to encapsulate the necessary information required
 * for requesting participant details, primarily consisting of a list of user IDs.
 *
 * @property userIds A list of unique user IDs for which participant information is requested.
 */
@Serializable
data class ParticipantsRequest(
    val userIds: List<String>
)