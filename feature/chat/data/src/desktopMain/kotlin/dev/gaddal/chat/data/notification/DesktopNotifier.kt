package dev.gaddal.chat.data.notification

import dev.gaddal.chat.domain.chat.ChatConnectionClient
import dev.gaddal.chat.domain.chat.ChatRepository
import dev.gaddal.core.domain.auth.SessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * A helper class responsible for observing and processing notifications for a desktop environment.
 *
 * The `DesktopNotifier` class listens to incoming chat messages and filters for messages that
 * are not sent by the current logged-in user. It then processes the message to create a notification
 * payload containing information such as the sender's name and chat details.
 *
 * This class combines multiple data sources, including the chat message stream, session storage,
 * and chat repository, to generate user-specific desktop notifications for incoming messages.
 *
 * @property chatConnectionClient A client for observing real-time chat messages.
 * @property sessionStorage Storage for managing authentication information.
 * @property chatRepository Repository to query chat-related data, such as participants and chat metadata.
 */
class DesktopNotifier(
    private val chatConnectionClient: ChatConnectionClient,
    private val sessionStorage: SessionStorage,
    private val chatRepository: ChatRepository
) {
    /**
     * Data class representing a notification payload.
     *
     * This class encapsulates the data required to display a notification,
     * including the title and a descriptive message. It is typically used
     * to generate notifications for events like chat messages in a desktop
     * context.
     *
     * @property title The title of the notification. Usually displays the name(s)
     * of participants or an identifier for the event.
     * @property message The descriptive content of the notification. Typically includes
     * the sender's name and the message details.
     */
    data class NotificationPayload(
        val title: String,
        val message: String
    )

    /**
     * Observes new incoming chat messages and emits notification payloads for messages sent by other users.
     *
     * This method listens to chat message updates and authentication information. It filters out messages
     * sent by the currently authenticated user and produces a notification payload containing a title
     * and message content for relevant incoming messages. Notifications are only generated for unique
     * messages with updated content to prevent duplicates.
     *
     * @return A Flow emitting instances of NotificationPayload, which include the title and message
     * content of the notifications derived from chat updates.
     */
    fun observeNewNotifications(): Flow<NotificationPayload> {
        return combine(
            chatConnectionClient.chatMessages,
            sessionStorage.observeAuthInfo(),
        ) { chatMessage, authInfo ->
            val currentUserId = authInfo?.user?.id
            if (chatMessage.senderId != currentUserId) {
                (chatMessage to currentUserId)
            } else null
        }
            .filterNotNull()
            .distinctUntilChangedBy { (message, _) -> message.id }
            .map { (message, currentUserId) ->
                val chatInfo = chatRepository.getChatInfoById(message.chatId).firstOrNull()

                val senderName = chatInfo?.chat?.participants?.find {
                    it.userId == message.senderId
                }?.username

                val notificationTitle = chatInfo?.chat?.participants?.let { participants ->
                    participants
                        .filter { it.userId != currentUserId }
                        .sortedBy { it.username }
                        .joinToString(", ") { it.username }
                }

                NotificationPayload(
                    title = notificationTitle ?: "Unknown",
                    message = "$senderName: ${message.content}"
                )
            }
    }
}