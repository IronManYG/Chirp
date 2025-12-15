package dev.gaddal.chirp.windows

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.window.FrameWindowScope
import java.awt.event.WindowEvent
import java.awt.event.WindowFocusListener

/**
 * Observes the focus state of the associated window and triggers a callback
 * whenever the focus state changes.
 *
 * @param onFocusChanged A callback function invoked with a boolean indicating
 * whether the window has gained or lost focus. The parameter is `true` if the
 * window is focused and `false` otherwise.
 */
@Composable
fun FrameWindowScope.FocusObserver(
    onFocusChanged: (Boolean) -> Unit
) {
    DisposableEffect(Unit) {
        val focusListener = object : WindowFocusListener {
            override fun windowGainedFocus(p0: WindowEvent?) {
                onFocusChanged(true)
            }

            override fun windowLostFocus(p0: WindowEvent?) {
                onFocusChanged(false)
            }
        }

        window.addWindowFocusListener(focusListener)

        onDispose {
            window.removeWindowFocusListener(focusListener)
        }
    }
}