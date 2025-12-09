package dev.gaddal.chat.domain.notification

import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult

/**
 * Service interface for managing device tokens.
 */
interface DeviceTokenService {

    /**
     * Registers a device token for a specific platform.
     *
     * @param token The unique identifier for the device token to be registered.
     * @param platform The platform associated with the token (e.g., Android, iOS).
     * @return An [EmptyResult] indicating success or failure of the operation, with potential error information as [DataError.Remote].
     */
    suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyResult<DataError.Remote>

    /**
     * Unregisters a device token, removing it from the system.
     *
     * @param token The device token to be unregistered.
     * @return A result indicating success or failure. Returns an `EmptyResult` which
     *         contains a `DataError.Remote` in case of failure.
     */
    suspend fun unregisterToken(
        token: String
    ): EmptyResult<DataError.Remote>
}