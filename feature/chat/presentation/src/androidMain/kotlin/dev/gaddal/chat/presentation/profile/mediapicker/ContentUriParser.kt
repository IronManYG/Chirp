package dev.gaddal.chat.presentation.profile.mediapicker

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * A utility class for parsing content URIs and retrieving associated data.
 *
 * This class provides methods to read the binary data of a URI and retrieve its MIME type.
 * It utilizes Android's content resolver to process the given URI.
 *
 * @constructor Creates an instance of ContentUriParser with the given application context.
 * @param context The application context used to interact with the content resolver.
 */
class ContentUriParser(
    private val context: Context
) {
    /**
     * Reads the contents of the provided URI as a byte array.
     *
     * @param uri The URI pointing to the content to be read.
     * @return A byte array containing the contents of the URI, or null if the content could not be read.
     */
    suspend fun readUri(uri: Uri): ByteArray? {
        return withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        }
    }

    /**
     * Retrieves the MIME type of the given URI.
     *
     * This method first tries to obtain the MIME type using the system's content resolver.
     * If the MIME type cannot be determined that way, it attempts to resolve it based on the file extension.
     *
     * @param uri The URI for which the MIME type needs to be determined.
     * @return The MIME type of the URI, or null if it cannot be determined.
     */
    fun getMimeType(uri: Uri): String? {
        return context.contentResolver.getType(uri)
            ?: getMimeTypeFromExtension(uri)
    }

    /**
     * Retrieves the MIME type of a file based on its extension extracted from the given Uri.
     *
     * @param uri The Uri of the file to determine its MIME type.
     * @return The MIME type as a String if the extension is valid and recognized, or null otherwise.
     */
    private fun getMimeTypeFromExtension(uri: Uri): String? {
        val extension = uri.toString().substringAfterLast(".", "")
        return if (extension.isNotBlank()) {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        } else null
    }
}