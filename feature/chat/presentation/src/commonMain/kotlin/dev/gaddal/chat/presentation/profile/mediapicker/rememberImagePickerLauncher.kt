package dev.gaddal.chat.presentation.profile.mediapicker

import androidx.compose.runtime.Composable

/**
 * Creates and remembers an [ImagePickerLauncher] for selecting an image from the device's gallery.
 *
 * This composable function is a platform-agnostic way to handle image picking. It returns a launcher
 * that, when triggered, opens the system's image selection UI. The result is delivered
 * via the `onResult` callback as a [PickedImageData] object, containing the image's byte array and MIME type.
 *
 * @param onResult A callback lambda that receives the [PickedImageData] upon successful selection.
 * @return An [ImagePickerLauncher] instance with a `launch` method to start the image picking process.
 */
@Composable
expect fun rememberImagePickerLauncher(
    onResult: (PickedImageData) -> Unit
): ImagePickerLauncher

/**
 * A launcher for the platform-specific image picker.
 *
 * This class provides a unified interface to trigger the native image picker
 * on different platforms (e.g., Android, iOS, Desktop). It is obtained by calling
 * `rememberImagePickerLauncher` within a Composable function.
 *
 * @param onLaunch A lambda that encapsulates the platform-specific logic to start the image picker.
 *                 This is provided by the `rememberImagePickerLauncher` implementation.
 */
class ImagePickerLauncher(
    private val onLaunch: () -> Unit
) {
    /**
     * Triggers the launch action associated with the image picker.
     * This method calls the `onLaunch` callback to initiate the functionality.
     * Typically used to open an image picker for users to select a profile picture.
     */
    fun launch() {
        onLaunch()
    }
}

/**
 * Represents the data of an image selected by the user.
 *
 * This data class encapsulates the binary data of the image and its corresponding
 * MIME type, providing a standardized way to handle image data selected from a media picker.
 *
 * @property bytes The raw byte array of the selected image.
 * @property mimeType The MIME type of the image (e.g., "image/jpeg", "image/png"), or null if it cannot be determined.
 */
class PickedImageData(
    val bytes: ByteArray,
    val mimeType: String?
)