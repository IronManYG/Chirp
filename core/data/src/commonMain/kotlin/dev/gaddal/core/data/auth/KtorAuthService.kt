package dev.gaddal.core.data.auth

import dev.gaddal.core.data.dto.requests.RegisterRequest
import dev.gaddal.core.data.networking.post
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

/**
 * Implementation of the [AuthService] interface using Ktor's [HttpClient] to handle
 * authentication-related operations, such as user registration.
 *
 * This class utilizes HTTP requests to communicate with a remote authentication
 * service, providing methods that wrap responses in structured result types.
 *
 * @constructor Initializes the [KtorAuthService] with the provided [httpClient].
 *
 * @param httpClient An instance of Ktor's [HttpClient], used to perform HTTP operations.
 */
class KtorAuthService(
    private val httpClient: HttpClient
) : AuthService {

    /**
     * Registers a new user with the provided email, username, and password.
     *
     * @param email The email address of the user to register.
     * @param username The username of the user to register.
     * @param password The password of the user to register.
     * @return An `EmptyResult` indicating the outcome of the registration operation,
     *         with any potential `DataError.Remote` indicating a failure.
     */
    override suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/register",
            body = RegisterRequest(
                email = email,
                username = username,
                password = password
            )
        )
    }
}