package dev.gaddal.core.data.auth

import dev.gaddal.core.data.dto.AuthInfoSerializable
import dev.gaddal.core.data.dto.requests.EmailRequest
import dev.gaddal.core.data.dto.requests.LoginRequest
import dev.gaddal.core.data.dto.requests.RegisterRequest
import dev.gaddal.core.data.mappers.toDomain
import dev.gaddal.core.data.networking.get
import dev.gaddal.core.data.networking.post
import dev.gaddal.core.domain.auth.AuthInfo
import dev.gaddal.core.domain.auth.AuthService
import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result
import dev.gaddal.core.domain.util.map
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
     * Authenticates a user with the provided email and password.
     *
     * @param email The email address of the user attempting to authenticate.
     * @param password The corresponding password of the user.
     * @return A `Result` containing either `AuthInfo` upon successful authentication
     *         or `DataError.Remote` in case of a failure during the authentication process.
     */
    override suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote> {
        return httpClient.post<LoginRequest, AuthInfoSerializable>(
            route = "/auth/login",
            body = LoginRequest(
                email = email,
                password = password
            )
        ).map { authInfoSerializable ->
            authInfoSerializable.toDomain()
        }
    }

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

    /**
     * Resends the verification email to the specified email address.
     *
     * @param email The email address to which the verification email will be sent.
     * @return An `EmptyResult` indicating the success or failure of the operation,
     *         with a potential `DataError.Remote` detailing any error that occurred.
     */
    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post(
            route = "/auth/resend-verification",
            body = EmailRequest(email),
        )
    }

    /**
     * Verifies an email using the provided token.
     *
     * @param token The verification token associated with the email.
     * @return An `EmptyResult` indicating the outcome of the verification process,
     *         with potential for a `DataError.Remote` in case of errors.
     */
    override suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote> {
        return httpClient.get(
            route = "/auth/verify",
            queryParams = mapOf("token" to token)
        )
    }

    /**
     * Initiates the process of resetting the password for an account associated with the provided email.
     *
     * @param email The email address of the account for which the password reset process is being requested.
     * @return An `EmptyResult` indicating the success or failure of the operation,
     *         with a potential `DataError.Remote` detailing any error that occurred.
     */
    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return httpClient.post<EmailRequest, Unit>(
            route = "/auth/forgot-password",
            body = EmailRequest(email)
        )
    }
}