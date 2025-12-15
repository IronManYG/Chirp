package dev.gaddal.chirp.deeplink

import dev.gaddal.chirp.navigation.ExternalUriHandler
import java.awt.Desktop
import javax.swing.SwingUtilities

/**
 * Handles deep linking for the desktop version of the application.
 *
 * This singleton object is responsible for processing incoming deep links
 * or URI events and ensures that valid URIs are appropriately forwarded
 * for further handling. It sets up a URI handler if supported by the desktop
 * platform. The class also validates URIs against a predefined list of supported
 * patterns.
 */
object DesktopDeepLinkHandler {

    /**
     * A list of predefined regular expressions representing URI patterns supported by the application.
     *
     * This collection establishes which URI formats are considered valid for handling within the application.
     * It is primarily used to verify incoming deep links or URIs against these patterns to determine their compatibility.
     */
    val supportedUriPatterns = listOf(
        Regex("^chirp://.*"),
        Regex("^https?://chirp\\.pl-coding\\.com/.*"),
    )

    /**
     * Indicates whether the initialization process has been completed.
     * This property is used to ensure that the setup process is executed only once.
     * It helps to prevent redundant or repeated initialization logic.
     */
    private var isInitialized = false

    /**
     * Sets up the application to handle deep links through the system's URI mechanism.
     *
     * This method performs the following actions:
     * - Checks if the functionality is already initialized to avoid repeated setup.
     * - Verifies if the desktop environment supports the necessary features.
     * - Configures a handler for opening URIs if supported by the desktop environment.
     * - Dispatches the handling of URIs to the `processUri` method on the Event Dispatch Thread (EDT).
     *
     * This setup is designed to enable applications to respond to externally triggered URI events,
     * allowing integration with deep link functionality.
     *
     * Any exceptions encountered during the setup process are caught and printed to the error stream,
     * ensuring the application does not crash due to issues related to deep link handling setup.
     */
    fun setup() {
        if (isInitialized) {
            return
        }

        if (!Desktop.isDesktopSupported()) {
            return
        }

        try {
            val desktop = Desktop.getDesktop()
            if (desktop.isSupported(Desktop.Action.APP_OPEN_URI)) {
                desktop.setOpenURIHandler { event ->
                    val uri = event.uri.toString()
                    SwingUtilities.invokeLater {
                        processUri(uri)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Processes the given URI by cleaning it and handling it through the external URI handler
     * if the URI is valid.
     *
     * @param uri The raw URI string that needs to be processed.
     */
    private fun processUri(uri: String) {
        val cleanUri = uri.trim('"', ' ')

        if (!isValidUri(uri)) {
            return
        }

        ExternalUriHandler.onNewUri(cleanUri)
    }

    /**
     * Validates if the provided URI matches any of the predefined supported patterns.
     *
     * @param uri The URI string to validate.
     * @return `true` if the URI matches one of the supported patterns, `false` otherwise.
     */
    private fun isValidUri(uri: String): Boolean {
        return supportedUriPatterns.any { it.matches(uri) }
    }
}