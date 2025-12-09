package dev.gaddal.chirp

import dev.gaddal.chat.data.notification.IosDeviceTokenHolder

/**
 * A bridge object that serves as an interface for updating the iOS device token.
 *
 * This object provides a method to invoke the `updateToken` functionality from
 * the `IosDeviceTokenHolder`. It acts as a wrapper to allow integration of
 * the token management utility into various parts of the codebase.
 */
object IosDeviceTokenHolderBridge {
    /**
     * Updates the device token stored in the `IosDeviceTokenHolder`.
     *
     * This method replaces the current device token with the new one provided as an input parameter.
     * It is used to ensure that the application always holds the latest device token, which is necessary
     * for push notification services on iOS platforms.
     *
     * @param token The new device token to update. This is a non-null string representing the current device token.
     */
    fun updateToken(token: String) {
        IosDeviceTokenHolder.updateToken(token)
    }
}