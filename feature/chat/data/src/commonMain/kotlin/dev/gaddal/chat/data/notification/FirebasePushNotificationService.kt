package dev.gaddal.chat.data.notification

import dev.gaddal.chat.domain.notification.PushNotificationService
import kotlinx.coroutines.flow.Flow

/**
 * Service implementation for handling push notification device tokens using Firebase Cloud Messaging (FCM).
 *
 * This class utilizes Firebase's messaging API to observe and emit the current device's
 * notification token, which is required for registering the device with FCM to receive push notifications.
 *
 * @constructor Creates an instance of the FirebasePushNotificationService with the specified dependencies.
 */
expect class FirebasePushNotificationService : PushNotificationService {
    /**
     * Observes and provides updates for the current device token used for push notifications.
     *
     * This method returns a flow that emits the device token as a string whenever there is a change.
     * The flow allows clients to react to device token updates dynamically. The emitted token may be `null`
     * if no token is available or has been generated.
     *
     * @return A [Flow] emitting the current device token as a [String], or `null` if the token is unavailable.
     */
    override fun observeDeviceToken(): Flow<String?>
}