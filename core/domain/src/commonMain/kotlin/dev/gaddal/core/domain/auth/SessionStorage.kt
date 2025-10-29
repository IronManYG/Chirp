package dev.gaddal.core.domain.auth

import kotlinx.coroutines.flow.Flow

/**
 * Interface for managing session-related storage operations, primarily focused on authentication information.
 *
 * This abstraction allows for observing and updating the stored authentication information,
 * enabling components to react to changes in session state and manage user authentication data efficiently.
 */
interface SessionStorage {
    fun observeAuthInfo(): Flow<AuthInfo?>
    suspend fun set(info: AuthInfo?)
}