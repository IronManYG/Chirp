package dev.gaddal.chat.presentation.profile.mediapicker

import androidx.compose.runtime.Composable
import androidx.compose.ui.draganddrop.DragAndDropTarget

/**
 * Remembers a drag-and-drop target in a Composable UI that handles hover and drop events.
 *
 * This function provides a cross-platform mechanism for implementing drag-and-drop functionality,
 * where the target can respond to hover states and execute actions when an item is dropped.
 * The dropped item is represented by [PickedImageData], encapsulating the image's binary data
 * and MIME type.
 *
 * @param onHover A callback triggered when an item is dragged over the target,
 *                with a boolean indicating whether the target is being hovered.
 * @param onDrop A callback triggered when an item is dropped onto the target.
 *               The dropped item's details are provided as a [PickedImageData].
 * @return A [DragAndDropTarget] instance allowing the UI to register as a target
 *         for drag-and-drop operations.
 */
@Composable
expect fun rememberDragAndDropTarget(
    onHover: (Boolean) -> Unit,
    onDrop: (PickedImageData) -> Unit
): DragAndDropTarget