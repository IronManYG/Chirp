package dev.gaddal.chat.data.participant

import dev.gaddal.chat.domain.models.ChatParticipant
import dev.gaddal.chat.domain.participant.ChatParticipantRepository
import dev.gaddal.chat.domain.participant.ChatParticipantService
import dev.gaddal.core.domain.auth.SessionStorage
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.onSuccess
import kotlinx.coroutines.flow.first

/**
 * Implementation of the `ChatParticipantRepository` designed for offline-first operations.
 *
 * This repository coordinates fetching and storing chat participant data, with a focus on
 * utilizing local storage when possible and falling back to remote services as needed.
 * It integrates with `SessionStorage` for updating and observing authentication information
 * and `ChatParticipantService` for retrieving chat participant details.
 *
 * @param sessionStorage A `SessionStorage` instance used for managing session-related data.
 * @param chatParticipantService A `ChatParticipantService` instance for fetching participant data.
 */
class OfflineFirstChatParticipantRepository(
    private val sessionStorage: SessionStorage,
    private val chatParticipantService: ChatParticipantService
) : ChatParticipantRepository {

    /**
     * Fetches the local chat participant data and updates the session storage with the participant's details.
     *
     * The method retrieves the local participant's information, which includes the participant's user ID,
     * username, and profile picture URL. Once this data is fetched successfully, it updates the
     * session storage with the corresponding authentication information, modifying the stored user details
     * to match those of the fetched participant.
     *
     * @return A [Result] containing either the [ChatParticipant] instance on success or a [DataError] on failure.
     */
    override suspend fun fetchLocalParticipant(): Result<ChatParticipant, DataError> {
        return chatParticipantService
            .getLocalParticipant()
            .onSuccess { participant ->
                val currentAuthInfo = sessionStorage.observeAuthInfo().first()
                sessionStorage.set(
                    currentAuthInfo?.copy(
                        user = currentAuthInfo.user.copy(
                            id = participant.userId,
                            username = participant.username,
                            profilePictureUrl = participant.profilePictureUrl
                        )
                    )
                )
            }
    }
}