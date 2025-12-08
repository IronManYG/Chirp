package dev.gaddal.chat.presentation.profile.mediapicker

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

/**
 * A Composable function that creates and remembers an `ImagePickerLauncher` for Android.
 * This launcher allows the user to select an image from the device's photo picker.
 *
 * When an image is selected, its content is read into a byte array and, along with its MIME type,
 * is encapsulated in a [PickedImageData] object. This object is then delivered
 * through the [onResult] callback.
 *
 * The image processing (reading the URI) is performed within a coroutine to avoid blocking the main thread.
 *
 * @param onResult A lambda that will be invoked with the [PickedImageData] of the selected image.
 * @return An instance of [ImagePickerLauncher] that can be used to trigger the photo picker.
 */
@Composable
actual fun rememberImagePickerLauncher(
    onResult: (PickedImageData) -> Unit
): ImagePickerLauncher {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val parser = ContentUriParser(context)
            val mimeType = parser.getMimeType(uri)

            scope.launch {
                val data = PickedImageData(
                    bytes = parser.readUri(uri) ?: return@launch,
                    mimeType = mimeType
                )
                onResult(data)
            }
        }
    }

    return remember {
        ImagePickerLauncher(
            onLaunch = {
                photoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }
        )
    }
}