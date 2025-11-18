package dev.gaddal.chat.data.network

import kotlinx.coroutines.flow.Flow

/**
 * A platform-agnostic observer for tracking network connectivity status.
 * This class allows clients to monitor whether the device is connected to a network.
 *
 * @property isConnected A Flow emitting the current connectivity status as a Boolean.
 *                       Emits `true` when the device is connected and `false` otherwise.
 */
expect class ConnectivityObserver {
    val isConnected: Flow<Boolean>
}