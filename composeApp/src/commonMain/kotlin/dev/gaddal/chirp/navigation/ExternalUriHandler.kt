package dev.gaddal.chirp.navigation

/**
 * Handles external URI events by providing a listener mechanism to manage new URI updates.
 *
 * This singleton object is designed to manage the handling of external URIs in the application.
 * It allows registering a listener that gets notified whenever a new URI is received. If a listener
 * is not currently available when a URI is received, the URI is cached and delivered when a listener
 * is later set.
 *
 * ### Key Functionalities:
 * - **Listener Registration**: Allows setting a listener to handle incoming URI events. The listener
 *   is invoked immediately with a cached URI if available.
 * - **URI Caching**: Stores the most recent URI if no listener is currently present to handle the event.
 *   The cached URI is cleared once it's delivered to a listener.
 *
 * This object is typically utilized in instances where external sources (e.g., deep links) need to
 * inform the app about navigation targets or other actions tied to a URI. It ensures that received
 * URIs are either processed immediately or held until a handler is ready.
 *
 * Thread Safety: This class is not thread-safe and is intended to be used in the context of a
 * single-threaded UI framework or controlled environment.
 */
object ExternalUriHandler {

    private var cached: String? = null

    var listener: ((uri: String) -> Unit)? = null
        set(value) {
            field = value
            if(value != null) {
                cached?.let {
                    value.invoke(it)
                }
                cached = null
            }
        }

    fun onNewUri(uri: String) {
        cached = uri
        listener?.let {
            it.invoke(uri)
            cached = null
        }
    }
}