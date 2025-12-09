package dev.gaddal.chat.data.notification

import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging
import dev.gaddal.chat.domain.notification.PushNotificationService
import dev.gaddal.core.domain.logging.ChirpLogger
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.coroutineContext

/**
 * A service implementation of [PushNotificationService] for managing Firebase Cloud Messaging (FCM) tokens.
 *
 * This class provides functionality for observing and emitting the current FCM device token,
 * which is used for registering the device with Firebase for push notifications. It utilizes
 * a [ChirpLogger] for logging events and errors related to token retrieval.
 *
 * @param logger The [ChirpLogger] instance used for logging informational and error messages.
 */
actual class FirebasePushNotificationService(
    private val logger: ChirpLogger
) : PushNotificationService {

    /**
     * Observes and emits the device's Firebase Cloud Messaging (FCM) token.
     *
     * This method emits the initial FCM token when it is successfully retrieved using the Firebase Messaging API.
     * If an error occurs while fetching the token, a `null` value is emitted.
     *
     * @return A [Flow] that emits the FCM token as a [String], or `null` if the token cannot be retrieved.
     */
    actual override fun observeDeviceToken(): Flow<String?> = flow {
        try {
            val fcmToken = Firebase.messaging.token.await()
            logger.info("Initial FCM token received: $fcmToken")
            emit(fcmToken)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            logger.error("Failed to get FCM token", e)
            emit(null)
        }
    }
}