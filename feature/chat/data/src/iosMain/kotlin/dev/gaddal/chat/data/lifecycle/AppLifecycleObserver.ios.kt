package dev.gaddal.chat.data.lifecycle

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import platform.Foundation.NSNotificationCenter
import platform.Foundation.NSOperationQueue
import platform.UIKit.UIApplication
import platform.UIKit.UIApplicationDidBecomeActiveNotification
import platform.UIKit.UIApplicationDidEnterBackgroundNotification
import platform.UIKit.UIApplicationState
import platform.UIKit.UIApplicationWillEnterForegroundNotification
import platform.UIKit.UIApplicationWillResignActiveNotification

/**
 * Observes the application's lifecycle to determine its foreground or background state.
 *
 * This implementation provides functionality specific to the target platform, enabling
 * monitoring of the app's visibility within the user's environment. It emits updates
 * using a [Flow] to reflect changes in the app's lifecycle state.
 *
 * This is particularly useful in scenarios where the app's active or inactive state
 * needs to be tracked to manage resources, update UI components, or enable specific
 * user interactions.
 */
actual class AppLifecycleObserver {
    /**
     * A Flow that emits the current foreground status of the application.
     *
     * This property observes the lifecycle notifications of the application and provides
     * real-time updates on whether the app is in the foreground or background. It sends
     * `true` when the app is active or transitioning to active states, such as becoming
     * active or entering the foreground, and `false` when the app is moving to inactive
     * or background states, like resigning active status or entering the background.
     *
     * The state changes are monitored using `NSNotificationCenter` observers, which listen
     * to relevant application lifecycle events (e.g., `UIApplicationDidBecomeActiveNotification`,
     * `UIApplicationWillEnterForegroundNotification`, `UIApplicationDidEnterBackgroundNotification`,
     * and `UIApplicationWillResignActiveNotification`).
     *
     * This flow ensures continuous emission of the app's foreground status, and cleans up the
     * associated observers when it is no longer being collected.
     */
    actual val isInForeground: Flow<Boolean> = callbackFlow {
        val currentState = UIApplication.sharedApplication.applicationState
        val isCurrentlyInForeground = when (currentState) {
            UIApplicationState.UIApplicationStateActive -> true
            // App itself is active, but could be that notification center is dragged down
            // or there's an ongoing phone call
            UIApplicationState.UIApplicationStateInactive -> true
            else -> false
        }
        send(isCurrentlyInForeground)

        val notificationCenter = NSNotificationCenter.defaultCenter

        val foregroundObserver = notificationCenter.addObserverForName(
            name = UIApplicationDidBecomeActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) {
            trySend(true)
        }

        val willEnterForegroundObserver = notificationCenter.addObserverForName(
            name = UIApplicationWillEnterForegroundNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) {
            trySend(true)
        }

        val backgroundObserver = notificationCenter.addObserverForName(
            name = UIApplicationDidEnterBackgroundNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) {
            trySend(false)
        }

        val willResignActiveObserver = notificationCenter.addObserverForName(
            name = UIApplicationWillResignActiveNotification,
            `object` = null,
            queue = NSOperationQueue.mainQueue
        ) {
            trySend(false)
        }

        awaitClose {
            notificationCenter.removeObserver(foregroundObserver)
            notificationCenter.removeObserver(willEnterForegroundObserver)
            notificationCenter.removeObserver(backgroundObserver)
            notificationCenter.removeObserver(willResignActiveObserver)
        }
    }
}