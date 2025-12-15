package dev.gaddal.chat.data.notification

import dev.gaddal.chat.domain.notification.PushNotificationService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/**
 * A platform-specific implementation of [PushNotificationService] for Firebase Cloud Messaging.
 *
 * This desktop implementation provides an empty implementation since push notifications
 * are not supported on desktop platforms.
 */
actual class FirebasePushNotificationService : PushNotificationService {
    actual override fun observeDeviceToken(): Flow<String?> {
        return emptyFlow()
    }
}