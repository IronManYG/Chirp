package dev.gaddal.chat.presentation.profile.mediapicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import chirp.feature.chat.presentation.generated.resources.Res
import chirp.feature.chat.presentation.generated.resources.select_a_profile_picture
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.jetbrains.compose.resources.stringResource
import java.awt.FileDialog
import java.awt.Frame
import java.io.File
import java.io.FilenameFilter
import java.nio.file.Files
import javax.swing.SwingUtilities
import kotlin.coroutines.resume

/**
 * Creates and remembers a platform-specific implementation of [ImagePickerLauncher] for image selection.
 *
 * This function provides an image picker launcher that can be used in a Compose application
 * to open the system's image selector. Upon receiving a result, it triggers the provided
 * `onResult` callback with the selected image's data encapsulated in a [PickedImageData] object.
 *
 * @param onResult A lambda function invoked with the [PickedImageData] of the selected image.
 * @return An [ImagePickerLauncher] instance, which can be used to trigger the image picking process.
 */
@Composable
actual fun rememberImagePickerLauncher(onResult: (PickedImageData) -> Unit): ImagePickerLauncher {
    val scope = rememberCoroutineScope()
    val dialogTitle = stringResource(Res.string.select_a_profile_picture)
    return remember {
        ImagePickerLauncher(
            onLaunch = {
                scope.launch {
                    pickImage(dialogTitle)?.let { data ->
                        onResult(data)
                    }
                }
            }
        )
    }
}

/**
 * Defines the list of supported file extensions for image files.
 *
 * This list is utilized in scenarios where image files need to be filtered,
 * such as drag-and-drop events or file picker dialogs, ensuring that only files
 * with the specified extensions are allowed for processing.
 *
 * Supported extensions are:
 * - png
 * - jpg
 * - jpeg
 * - webp
 */
internal val allowedImageExtensions = listOf(
    "png",
    "jpg",
    "jpeg",
    "webp",
)

/**
 * Retrieves the MIME type based on the file name's extension.
 *
 * @param fileName The name of the file from which the MIME type should be determined.
 * @return The corresponding MIME type as a string if the extension is recognized, or null if it is not.
 */
fun getMimeTypeFromFileName(fileName: String): String? {
    val extension = fileName.substringAfterLast(".", "").lowercase()
    return when (extension) {
        "png" -> "image/png"
        "jpg", "jpeg" -> "image/jpeg"
        "webp" -> "image/webp"
        else -> null
    }
}

/**
 * Launches a native file dialog to allow the user to select an image file.
 *
 * This function utilizes [SwingUtilities] to interface with the JVM's window management framework
 * (Swing) and converts the callback-based/blocking UI API into a Kotlin suspending function using
 * [suspendCancellableCoroutine].
 *
 * The coroutine suspends execution until the user either selects a file, cancels the dialog,
 * or an error occurs, at which point the continuation is resumed with the result.
 *
 * @param fileDialogTitle The title for the file dialog window, typically used to indicate
 * the purpose of the dialog to the user.
 * @return A [PickedImageData] object containing the binary data and MIME type of the
 * selected image, or `null` if no valid image is selected (dialog dismissed/cancelled)
 * or an error occurs.
 */
private suspend fun pickImage(fileDialogTitle: String): PickedImageData? {
    // We convert the callback-based/legacy Java API to a suspending API here.
    // This allows us to block the coroutine (suspending nature) while waiting for user input
    // without blocking the underlying thread.
    val file = suspendCancellableCoroutine<File?> { continuation ->
        var fileDialog: FileDialog? = null

        // Handle cancellation: If the coroutine is cancelled (e.g. user navigates away),
        // we ensure the native dialog is disposed of properly.
        continuation.invokeOnCancellation {
            SwingUtilities.invokeLater {
                fileDialog?.dispose()
            }
        }

        // Swing is a Java framework and doesn't know about Kotlin Coroutines.
        // We use invokeLater to ensure UI operations happen on the Event Dispatch Thread.
        SwingUtilities.invokeLater {
            try {
                fileDialog = FileDialog(Frame(), fileDialogTitle, FileDialog.LOAD)
                fileDialog.filenameFilter = FilenameFilter { _, name ->
                    allowedImageExtensions.any {
                        name.endsWith(it)
                    }
                }

                // This call blocks the Event Dispatch Thread until the user picks a file or closes the window.
                fileDialog.isVisible = true

                // Once the dialog is closed, code execution continues here.
                // We retrieve the file and resume the continuation to unblock the suspended coroutine.
                val file = File(fileDialog.directory, fileDialog.file)

                continuation.resume(file)
            } catch (_: Exception) {
                // If an error occurs during the UI operation, we resume with null to avoid hanging.
                continuation.resume(null)
            }
        }
    }

    return withContext(Dispatchers.IO) {
        if (file != null) {
            try {
                val mimeType = getMimeTypeFromFileName(file.name)
                val bytes = Files.readAllBytes(file.toPath())
                PickedImageData(
                    bytes = bytes,
                    mimeType = mimeType
                )
            } catch (_: Exception) {
                coroutineContext.ensureActive()
                null
            }
        } else {
            null
        }
    }
}