package dev.gaddal.core.domain.auth

import dev.gaddal.core.domain.util.DataError
import dev.gaddal.core.domain.util.EmptyResult

/**
 * Defines an interface for authentication services, providing methods for user registration
 * and email verification processes.
 *
 * This service is responsible for interacting with a backend or remote authentication system
 * to handle user-related actions such as registering new users and managing account verification
 * workflows. The operations within this interface are asynchronous and rely on the `EmptyResult` type
 * to encapsulate the result of each operation, which may be a success or a `DataError.Remote` failure.
 */
interface AuthService {
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
}