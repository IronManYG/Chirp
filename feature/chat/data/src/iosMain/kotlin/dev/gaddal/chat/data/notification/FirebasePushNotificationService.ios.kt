package dev.gaddal.chat.data.notification

import dev.gaddal.chat.domain.notification.PushNotificationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import platform.Foundation.NSUserDefaults
import platform.UIKit.UIApplication
import platform.UIKit.registerForRemoteNotifications

/**
 * A service implementation of [PushNotificationService] for managing Firebase Cloud Messaging (FCM) tokens on iOS.
 *
 * This class is responsible for observing and emitting the current FCM device token, which is used
 * for registering the device with Firebase for push notifications. It integrates the token handling
 * with the `IosDeviceTokenHolder` utility to manage token state.
 */
actual class FirebasePushNotificationService : PushNotificationService {
    /**
     * Observes and emits updates to the push notification device token on iOS.
     *
     * This function monitors the current state of the device token, retrieving the token
     * from `NSUserDefaults` if it is not already initialized. If no token is found,
     * it triggers registration for remote notifications.
     *
     * The returned flow emits values whenever the device token changes. The token may
     * initially be `null` if it has not been retrieved or updated yet.
     *
     * @return A [Flow] that emits the current device token as a [String], or `null` if no token is available.
     */
    actual override fun observeDeviceToken(): Flow<String?> {
        return IosDeviceTokenHolder
            .token
            .onStart {
                if (IosDeviceTokenHolder.token.value == null) {
                    val userDefaults = NSUserDefaults.standardUserDefaults
                    val fcmToken = userDefaults.stringForKey("FCM_TOKEN")

                    if (fcmToken != null) {
                        IosDeviceTokenHolder.updateToken(fcmToken)
                    } else {
                        UIApplication.sharedApplication.registerForRemoteNotifications()
                    }
                }
            }
    }
}