package dev.gaddal.chat.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.core.content.getSystemService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * A platform-specific implementation of `ConnectivityObserver` to monitor network connectivity.
 *
 * This class utilizes Android's `ConnectivityManager` to observe network status changes.
 * It emits connectivity statuses through a `Flow` stream, making it suitable for reactive programming
 * in applications that require real-time network monitoring.
 *
 * @constructor Creates an instance of `ConnectivityObserver` using the provided `Context`.
 * @param context The Android `Context` used to access the `ConnectivityManager`.
 */
actual class ConnectivityObserver(
    private val context: Context
) {
    /**
     * A reference to the system's `ConnectivityManager` service. Used to manage network-related operations,
     * such as monitoring network connectivity and registering network callbacks.
     */
    private val connectivityManager = context.getSystemService<ConnectivityManager>()!!

    /**
     * Provides a Flow that emits Boolean values representing the device's current network connectivity state.
     *
     * This property emits `true` when the device is connected to a network capable of validated internet access,
     * and `false` otherwise. The connectivity state is updated dynamically based on changes in network availability
     * and capabilities using the default network callback mechanism.
     *
     * The Flow's data is driven by events from the system's `ConnectivityManager`, ensuring up-to-date information
     * about the network status. It can be used to observe connectivity changes over time for applications
     * requiring real-time updates on network availability.
     */
    actual val isConnected: Flow<Boolean> = callbackFlow {
        val initiallyConnected = connectivityManager.activeNetwork?.let { network ->
            connectivityManager.getNetworkCapabilities(network)?.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_VALIDATED
            )
        } ?: false

        send(initiallyConnected)

        val callback = object : ConnectivityManager.NetworkCallback() {
            /**
             * Called when a network becomes available.
             *
             * @param network The network that became available.
             */
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                trySend(true)
            }

            /**
             * Triggered when a network is lost. This method updates the connectivity status to indicate
             * that the network is no longer available.
             *
             * @param network The network that has been lost.
             */
            override fun onLost(network: Network) {
                super.onLost(network)
                trySend(false)
            }

            /**
             * Invoked when no network is available or the attempt to establish a network connection fails.
             * This method sends a `false` value to signal the unavailability of a network.
             */
            override fun onUnavailable() {
                super.onUnavailable()
                trySend(false)
            }

            /**
             * Called when the capabilities of an active network change.
             *
             * @param network The network whose capabilities have changed.
             * @param networkCapabilities The updated capabilities of the provided network.
             */
            override fun onCapabilitiesChanged(
                network: Network,
                networkCapabilities: NetworkCapabilities
            ) {
                super.onCapabilitiesChanged(network, networkCapabilities)
                val connected = networkCapabilities.hasCapability(
                    NetworkCapabilities.NET_CAPABILITY_VALIDATED
                )
                trySend(connected)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}