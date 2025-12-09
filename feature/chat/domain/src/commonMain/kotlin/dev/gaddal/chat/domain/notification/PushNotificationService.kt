package dev.gaddal.chat.domain.notification

import kotlinx.coroutines.flow.Flow

/**
 * A service interface for observing push notification device tokens.
 *
 * Implementations of this interface provide mechanisms to monitor the current device's
 * push notification token, which is used for registering the device with notification
 * platforms (e.g., Firebase Cloud Messaging).
 */
interface PushNotificationService {
    /**
     * Observes and emits updates to the device token.
     *
     * This function returns a flow of device tokens, which might be `null` if no token is available.
     * It automatically emits new values whenever the device token changes.
     *
     * @return A [Flow] that emits the device token as a [String], or `null` if the token is unavailable.
     */
    fun observeDeviceToken(): Flow<String?>
}