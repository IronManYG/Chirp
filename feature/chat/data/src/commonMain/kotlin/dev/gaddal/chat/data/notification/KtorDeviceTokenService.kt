package dev.gaddal.chat.data.notification

import dev.gaddal.chat.data.dto.request.RegisterDeviceTokenRequest
import dev.gaddal.chat.domain.notification.DeviceTokenService
import dev.gaddal.core.data.networking.delete
import dev.gaddal.core.data.networking.post
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

/**
 * Implementation of the [DeviceTokenService] using Ktor's HTTP client.
 * This service handles the registration and unregistration of device tokens.
 *
 * @property httpClient The Ktor [HttpClient] used for making HTTP requests.
 */
class KtorDeviceTokenService(
    private val httpClient: HttpClient
) : DeviceTokenService {

    /**
     * Registers a device token for receiving notifications.
     *
     * @param token The device token to be registered.
     * @param platform The platform of the device (e.g., Android, iOS).
     * @return An [EmptyResult] indicating success or a [DataError.Remote] error if the operation fails.
     */
    override suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/notification/register",
            body = RegisterDeviceTokenRequest(
                token = token,
                platform = platform
            )
        )
    }

    /**
     * Unregisters a previously registered device token from the notification service.
     *
     * @param token The device token that needs to be unregistered.
     * @return An [EmptyResult] containing either success or a [DataError.Remote] in case of failure.
     */
    override suspend fun unregisterToken(token: String): EmptyResult<DataError.Remote> {
        return httpClient.delete(
            route = "/notification/$token"
        )
    }
}