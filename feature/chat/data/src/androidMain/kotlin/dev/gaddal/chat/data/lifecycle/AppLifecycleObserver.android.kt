package dev.gaddal.chat.data.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

/**
 * Observes the application's lifecycle to determine its foreground or background state.
 *
 * This implementation provides a [Flow] emitting updates on whether the app is currently in the foreground.
 * It uses the Android lifecycle library to monitor lifecycle events of the application process.
 * The resulting [Flow] is updated whenever the app enters or exits the foreground, enabling
 * consumers of this class to perform operations tied to the app's visibility state.
 */
actual class AppLifecycleObserver {
    /**
     * A Flow that emits a boolean value indicating whether the application is currently in the foreground.
     *
     * This Flow observes the lifecycle of the application to detect changes in its state. It emits `true`
     * when the application moves to the foreground (i.e., reaches at least the `STARTED` lifecycle state),
     * and emits `false` when the application goes to the background (i.e., transitions to the `STOPPED` state).
     *
     * The updates are triggered by lifecycle events (`ON_START` and `ON_STOP`) using a dedicated
     * `LifecycleEventObserver`. The Flow operates on the main dispatcher to ensure lifecycle
     * updates are handled appropriately.
     *
     * This property is useful for monitoring and reacting to the application's visibility state
     * in real time.
     */
    actual val isInForeground: Flow<Boolean> = callbackFlow {
        val lifecycle = ProcessLifecycleOwner.get().lifecycle

        val isAtLeastStarted = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
        send(isAtLeastStarted)

        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> trySend(true)
                Lifecycle.Event.ON_STOP -> trySend(false)
                else -> Unit
            }
        }

        lifecycle.addObserver(observer)

        awaitClose {
            lifecycle.removeObserver(observer)
        }
    }.flowOn(Dispatchers.Main)
}