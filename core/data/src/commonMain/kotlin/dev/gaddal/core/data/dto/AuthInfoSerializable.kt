package dev.gaddal.core.data.dto

import kotlinx.serialization.Serializable

/**
 * Represents serializable authentication information for data transfer.
 *
 * This data class is designed to handle serialization and deserialization of authentication-related
 * data, such as tokens and user details, for communication with external systems like APIs or databases.
 * It includes the following information:
 *
 * @property accessToken The token used to access secure resources on behalf of the authenticated user.
 * @property refreshToken The token used to obtain a new access token when the current one expires.
 * @property user An instance of [UserSerializable] containing detailed user information.
 */
@Serializable
data class AuthInfoSerializable(
    val accessToken: String,
    val refreshToken: String,
    val user: UserSerializable
)