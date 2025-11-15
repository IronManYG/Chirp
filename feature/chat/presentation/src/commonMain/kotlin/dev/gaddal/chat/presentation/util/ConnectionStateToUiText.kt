package dev.gaddal.chat.presentation.util

import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.network_error
import chirp.feature.chat.presentation.generated.resources.offline
import chirp.feature.chat.presentation.generated.resources.online
import chirp.feature.chat.presentation.generated.resources.reconnecting
import chirp.feature.chat.presentation.generated.resources.unknown_error
import dev.gaddal.chat.domain.models.ConnectionState
import dev.gaddal.core.presentation.util.UiText

/**
 * Converts the current [ConnectionState] instance to a [UiText] representation
 * for use in the user interface.
 *
 * This function maps each [ConnectionState] to a corresponding string resource:
 * - DISCONNECTED -> offline
 * - CONNECTING -> reconnecting
 * - CONNECTED -> online
 * - ERROR_NETWORK -> network_error
 * - ERROR_UNKNOWN -> unknown_error
 *
 * @return A [UiText.Resource] instance containing the string resource associated
 * with the corresponding [ConnectionState].
 */
fun ConnectionState.toUiText(): UiText {
    val resource = when (this) {
        ConnectionState.DISCONNECTED -> Res.string.offline
        ConnectionState.CONNECTING -> Res.string.reconnecting
        ConnectionState.CONNECTED -> Res.string.online
        ConnectionState.ERROR_NETWORK -> Res.string.network_error
        ConnectionState.ERROR_UNKNOWN -> Res.string.unknown_error
    }
    return UiText.Resource(resource)
}