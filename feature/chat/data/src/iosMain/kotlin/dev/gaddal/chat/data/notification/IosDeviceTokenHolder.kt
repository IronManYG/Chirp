package dev.gaddal.chat.data.notification

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * A utility object responsible for holding and updating the iOS device token.
 *
 * This object is used to store and observe the current iOS device token, which is essential
 * for enabling push notifications in the application. The stored token can be updated and
 * observed through exposed methods and flows.
 */
object IosDeviceTokenHolder {

    /**
     * Represents an internal mutable state holder for the current push notification device token.
     *
     * This state is used to store and manage the device token, which is a unique identifier
     * for registering the device with push notification services. The token can be updated
     * and observed by other components of the system to reflect changes in the device's
     * token state.
     *
     * The value is initialized to `null` and can later be set to the actual token string
     * when it becomes available.
     */
    private val _token = MutableStateFlow<String?>(null)

    /**
     * A read-only state flow that emits the current device token.
     *
     * The `token` flow represents the active state of the device token used for push notifications.
     * It is backed by a mutable state flow and allows observers to monitor changes to the token.
     * The token may initially be `null` if it has not yet been retrieved or updated.
     */
    val token = _token.asStateFlow()

    /**
     * Updates the stored device token value.
     *
     * @param token The new device token to be stored. Can be `null` if no token is available.
     */
    fun updateToken(token: String?) {
        _token.value = token
    }
}