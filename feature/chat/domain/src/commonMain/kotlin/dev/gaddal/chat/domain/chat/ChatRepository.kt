package dev.gaddal.chat.domain.chat

import dev.gaddal.chat.domain.models.Chat
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing and accessing chat data.
 *
 * This repository provides methods to retrieve and fetch chat information, designed to support
 * both live data streams and one-time network operations. The usage of `Flow` allows handling of
 * real-time updates, and the `Result` type ensures reliable error handling for remote operations.
 */
interface ChatRepository {
    /**
     * Retrieves a stream of chats as a Flow.
     *
     * This method provides a reactive stream of data representing the list of chats
     * that the user is currently a participant in. It emits updates whenever there
     * are changes to the list of chats, allowing for real-time monitoring of chat
     * activity such as new messages or updated last activity timestamps.
     *
     * @return A Flow emitting a list of Chat objects, where each Chat in the list contains
     * details such as the participants, last activity timestamp, and last message.
     */
    fun getChats(): Flow<List<Chat>>

    /**
     * Fetches an updated list of chats from a remote source.
     *
     * This method performs a network operation to retrieve the latest chat data and may
     * either return a successful result containing a list of chats or a failure result
     * with an associated error.
     *
     * @return A [Result] representing either a successful operation with a list of [Chat],
     *         or a failure with a [DataError.Remote] indicating the type of remote data error encountered.
     */
    suspend fun fetchChats(): Result<List<Chat>, DataError.Remote>
}