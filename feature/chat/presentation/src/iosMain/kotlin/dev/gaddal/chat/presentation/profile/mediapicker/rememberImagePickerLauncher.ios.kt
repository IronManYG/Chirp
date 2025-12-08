@file:OptIn(ExperimentalForeignApi::class)

package dev.gaddal.chat.presentation.profile.mediapicker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import platform.PhotosUI.PHPickerConfiguration
import platform.PhotosUI.PHPickerConfigurationSelectionOrdered
import platform.PhotosUI.PHPickerFilter
import platform.PhotosUI.PHPickerResult
import platform.PhotosUI.PHPickerViewController
import platform.PhotosUI.PHPickerViewControllerDelegateProtocol
import platform.UIKit.UIApplication
import platform.UniformTypeIdentifiers.UTType
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_group_create
import platform.darwin.dispatch_group_enter
import platform.darwin.dispatch_group_leave
import platform.darwin.dispatch_group_notify
import platform.posix.memcpy

/**
 * Creates and remembers an [ImagePickerLauncher] for selecting an image from the device's gallery for iOS.
 *
 * This composable function integrates with PHPickerViewController to provide native image picker functionality.
 * It configures the picker to allow single image selection and handles the result processing through
 * a PHPickerViewControllerDelegate. The selected image data is delivered as a [PickedImageData] object
 * through the `onResult` callback.
 *
 * @param onResult A lambda that processes the [PickedImageData] after successful image selection,
 *                 providing access to the image's byte array and MIME type.
 * @return An [ImagePickerLauncher] instance to launch the image picker UI.
 */
@Composable
actual fun rememberImagePickerLauncher(
    onResult: (PickedImageData) -> Unit
): ImagePickerLauncher {
    val scope = rememberCoroutineScope()
    val delegate = remember {
        object : NSObject(), PHPickerViewControllerDelegateProtocol {
            /**
             * Handles the completion of image picking from the `PHPickerViewController`.
             *
             * This method is triggered when the user finishes selecting images from the picker.
             * It processes the results, extracts image data, and converts it into a list of
             * `PickedImageData` objects. If successfully processed, the first valid image
             * from the selection is passed to the `onResult` callback.
             *
             * @param picker The instance of `PHPickerViewController` that invoked this delegate method.
             * @param didFinishPicking A list of selected items (potentially mixed types) from the picker.
             */
            override fun picker(picker: PHPickerViewController, didFinishPicking: List<*>) {
                picker.dismissViewControllerAnimated(true, null)

                val results = didFinishPicking.filterIsInstance<PHPickerResult>()

                val dispatchGroup = dispatch_group_create()
                val imageDataList = mutableListOf<PickedImageData>()

                for (result in results) {
                    dispatch_group_enter(dispatchGroup)

                    val itemProvider = result.itemProvider

                    val typeIdentifiers = itemProvider.registeredTypeIdentifiers
                    val primaryType = typeIdentifiers.firstOrNull() as? String

                    if (primaryType == null) {
                        dispatch_group_leave(dispatchGroup)
                        continue
                    }

                    val mimeType = UTType
                        .typeWithIdentifier(primaryType)
                        ?.preferredMIMEType

                    if (mimeType == null) {
                        dispatch_group_leave(dispatchGroup)
                        continue
                    }

                    itemProvider.loadDataRepresentationForTypeIdentifier(
                        typeIdentifier = primaryType
                    ) { nsData, nsError ->
                        scope.launch {
                            nsData?.let {
                                val bytes = ByteArray(it.length.toInt())

                                withContext(Dispatchers.Default) {
                                    memcpy(bytes.refTo(0), it.bytes, it.length)
                                }

                                imageDataList.add(
                                    PickedImageData(
                                        bytes = bytes,
                                        mimeType = mimeType
                                    )
                                )
                            }
                            dispatch_group_leave(dispatchGroup)
                        }
                    }

                    dispatch_group_notify(dispatchGroup, dispatch_get_main_queue()) {
                        scope.launch {
                            imageDataList.firstOrNull()?.let { item ->
                                onResult(item)
                            }
                        }
                    }
                }
            }
        }
    }

    return remember {
        val pickerViewController = PHPickerViewController(
            configuration = PHPickerConfiguration().apply {
                setSelectionLimit(1)
                setFilter(PHPickerFilter.imagesFilter)
                setSelection(PHPickerConfigurationSelectionOrdered)
            }
        )
        pickerViewController.delegate = delegate

        ImagePickerLauncher(
            onLaunch = {
                UIApplication.sharedApplication.keyWindow?.rootViewController?.presentViewController(
                    pickerViewController,
                    true,
                    null
                )
            }
        )
    }
}