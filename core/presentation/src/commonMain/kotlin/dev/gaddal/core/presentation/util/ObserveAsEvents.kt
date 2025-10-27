package dev.gaddal.core.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

/**
 * Observes a given flow as a series of events and invokes the specified callback for each emitted value.
 * This method ensures the observation is tied to the lifecycle owner and is only active when the lifecycle is in the STARTED state.
 *
 * @param flow The [Flow] of events to observe.
 * @param key1 An optional key used to determine recomposition. Default is null.
 * @param key2 An optional key used to determine recomposition. Default is null.
 * @param onEvent A suspend function that defines the action to be performed on each collected event.
 */
@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: suspend (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}