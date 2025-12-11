package dev.gaddal.chat.presentation.profile.mediapicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.draganddrop.DragAndDropEvent
import androidx.compose.ui.draganddrop.DragAndDropTarget

/**
 * Remembers a drag-and-drop target, enabling handling of drag-and-drop events.
 * Drag and drop is not supported on mobile platforms.
 *
 * @param onHover Callback invoked when an item hovers over the target. Not used on mobile.
 * @param onDrop Callback invoked when an item is dropped on the target. Not used on mobile.
 * @return A DragAndDropTarget instance that does nothing on mobile platforms.
 */
@Composable
actual fun rememberDragAndDropTarget(
    onHover: (Boolean) -> Unit,
    onDrop: (PickedImageData) -> Unit
): DragAndDropTarget {
    return remember {
        object : DragAndDropTarget {
            override fun onDrop(event: DragAndDropEvent): Boolean {
                return false
            }
        }
    }
}