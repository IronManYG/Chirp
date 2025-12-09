package dev.gaddal.chat.data.dto.request

import kotlinx.serialization.Serializable

/**
 * Represents a request to register a device token for push notifications.
 *
 * This data class encapsulates the information required to register a device token
 * associated with a specific platform for receiving push notifications. The token
 * is unique to the device, and the platform identifies the type of the device (e.g., Android, iOS).
 *
 * @property token The unique device token used for identification in the notification system.
 * @property platform The platform of the device associated with the token.
 */
@Serializable
data class RegisterDeviceTokenRequest(
    val token: String,
    val platform: String
)