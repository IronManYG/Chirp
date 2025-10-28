package dev.gaddal.core.data.mappers

import dev.gaddal.core.domain.auth.AuthInfo
import dev.gaddal.core.domain.auth.User
import dev.gaddal.core.data.dto.AuthInfoSerializable
import dev.gaddal.core.data.dto.UserSerializable

/**
 * Converts an instance of [AuthInfoSerializable] into its corresponding domain representation, [AuthInfo].
 *
 * This function maps the serializable data transfer object [AuthInfoSerializable] to the domain-specific
 * data class [AuthInfo] by copying the access token, refresh token, and transforming the user information
 * using the `toDomain` function.
 *
 * @return A domain-specific representation of authentication information encapsulated in [AuthInfo].
 */
fun AuthInfoSerializable.toDomain(): AuthInfo {
    return AuthInfo(
        accessToken = accessToken,
        refreshToken = refreshToken,
        user = user.toDomain()
    )
}

/**
 * Converts an instance of [UserSerializable] to the domain-level [User] representation.
 *
 * This function maps the properties from the serializable data model to the corresponding
 * domain model, enabling the use of user data within the application's core logic.
 *
 * @return A [User] instance containing the mapped properties from the [UserSerializable] object.
 */
fun UserSerializable.toDomain(): User {
    return User(
        id = id,
        email = email,
        username = username,
        hasVerifiedEmail = hasVerifiedEmail,
        profilePictureUrl = profilePictureUrl
    )
}