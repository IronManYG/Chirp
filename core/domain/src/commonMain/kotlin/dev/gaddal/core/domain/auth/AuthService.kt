package dev.gaddal.core.domain.auth

import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult
import dev.gaddal.core.domain.util.Result

/**
 * Defines an interface for authentication and authorization services, providing methods for user
 * authentication, registration, and account verification processes.
 *
 * This service is responsible for:
 * - User authentication through login credentials
 * - Managing authentication tokens (access and refresh tokens)
 * - User registration and account creation
 * - Email verification workflows
 * - Handling secure communication with the authentication backend
 *
 * All operations within this interface are asynchronous and return either a successful result
 * (with authentication data where applicable) or a [DataError.Remote] describing any failures
 * that occurred during the authentication process.
 */
interface AuthService {
    /**
     * Attempts to authenticate a user using the provided email and password.
     *
     * This function communicates with the authentication service to validate the
     * provided credentials. The operation may return either a successful result
     * containing authentication information or an error detailing the issue.
     *
     * @param email The email address of the user attempting to log in.
     * @param password The password associated with the user's account.
     * @return A `Result` instance containing either `AuthInfo` on success or `DataError.Remote` on failure.
     */
    suspend fun login(
        email: String,
        password: String
    ): Result<AuthInfo, DataError.Remote>

    /**
     * Attempts to register a new user with the specified email, username, and password.
     *
     * This function interacts with the authentication service to create a new user account. The operation
     * may result in either a successful registration or an error, which is wrapped in an `EmptyResult` type.
     *
     * @param email The email address to be associated with the new user account.
     * @param username The username to be assigned to the new user account.
     * @param password The password to be used for the new user account.
     * @return An `EmptyResult` that encapsulates either success or a `DataError.Remote` indicating the type of failure.
     */
    suspend fun register(
        email: String,
        username: String,
        password: String
    ): EmptyResult<DataError.Remote>

    /**
     * Resends a verification email to the specified email address.
     *
     * This function communicates with the authentication service to initiate the process
     * of resending a verification email. It is intended to be used when a user needs to verify
     * their email address but did not receive or has lost the original verification email.
     *
     * @param email The email address to which the verification email should be resent.
     * @return An `EmptyResult` containing either success or a `DataError.Remote` representing the failure type.
     */
    suspend fun resendVerificationEmail(
        email: String
    ): EmptyResult<DataError.Remote>

    /**
     * Attempts to verify a user's email using the provided token.
     *
     * This function interacts with the authentication service to validate the
     * email verification token. A successful operation indicates that the
     * email is verified, while an error result specifies the failure type.
     *
     * @param token The email verification token to be validated.
     * @return An `EmptyResult` encapsulating either success or a `DataError.Remote` describing the error type.
     */
    suspend fun verifyEmail(token: String): EmptyResult<DataError.Remote>
}