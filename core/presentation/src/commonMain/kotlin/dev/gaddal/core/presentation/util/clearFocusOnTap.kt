package dev.gaddal.core.presentation.util

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager

/**
 * Adds functionality to clear focus from currently focused components when the user taps
 * outside of them. Useful for dismissing keyboards or removing focus from text fields.
 *
 * @return A [Modifier] that clears focus on tap.
 */
@Composable
fun Modifier.clearFocusOnTap(): Modifier {
    val focusManager = LocalFocusManager.current
    return this.pointerInput(Unit) {
        detectTapGestures {
            focusManager.clearFocus()
        }
    }
}