package dev.gaddal.chat.data.network

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Network.nw_path_get_status
import platform.Network.nw_path_monitor_cancel
import platform.Network.nw_path_monitor_create
import platform.Network.nw_path_monitor_set_queue
import platform.Network.nw_path_monitor_set_update_handler
import platform.Network.nw_path_monitor_start
import platform.Network.nw_path_status_satisfiable
import platform.Network.nw_path_status_satisfied
import platform.darwin.dispatch_queue_create

/**
 * A platform-specific implementation of the `ConnectivityObserver` class.
 * This class tracks the network connectivity status using platform native APIs.
 *
 * This implementation utilizes `nw_path_monitor_create` and related functions to monitor
 * network connectivity changes and emits the status through a Kotlin Flow.
 */
actual class ConnectivityObserver {
    /**
     * Represents a Flow emitting the current network connectivity status.
     * The Flow emits `true` when the device has an active network connection
     * and `false` otherwise.
     *
     * This is a platform-specific implementation that monitors network path
     * changes to update the connectivity status in real-time.
     */
    actual val isConnected: Flow<Boolean> = callbackFlow {
        val pathMonitor = nw_path_monitor_create()

        val queue = dispatch_queue_create(
            NW_PATH_MONITOR_LABEL,
            null
        )

        nw_path_monitor_set_update_handler(pathMonitor) { path ->
            if (path != null) {
                val status = nw_path_get_status(path)

                val isConnected = when (status) {
                    nw_path_status_satisfied -> true
                    nw_path_status_satisfiable -> true
                    else -> false
                }

                trySend(isConnected)
            }
        }

        nw_path_monitor_set_queue(pathMonitor, queue)
        nw_path_monitor_start(pathMonitor)

        awaitClose {
            nw_path_monitor_cancel(pathMonitor)
        }
    }

    /**
     * Companion object for the ConnectivityObserver class.
     * This typically holds utility methods or constants associated with ConnectivityObserver.
     */
    companion object {
        /**
         * A constant that defines the label used when creating the dispatch queue
         * for the network path monitor in the `ConnectivityObserver` class.
         * It is utilized to uniquely identify and manage the background queue
         * for handling network connectivity updates.
         */
        private const val NW_PATH_MONITOR_LABEL =
            "dev.gaddal.chat.data.network.ConnectivityObserver"
    }
}