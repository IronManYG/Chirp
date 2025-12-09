package dev.gaddal.chat.data.notification

import com.google.firebase.messaging.FirebaseMessagingService
import dev.gaddal.chat.domain.notification.DeviceTokenService
import dev.gaddal.core.domain.auth.SessionStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

/**
 * ChirpFirebaseMessagingService is a FirebaseMessagingService implementation that handles tasks related
 * to Firebase Cloud Messaging (FCM) token management and registration.
 *
 * This service overrides the `onNewToken` method to handle token updates and automatically register
 * the new token with the backend system for push notifications.
 *
 * Dependencies for this class are injected using the dependency injection framework.
 *
 * @property deviceTokenService The `DeviceTokenService` instance used for registering new device tokens.
 * @property sessionStorage The `SessionStorage` instance used for observing authentication state.
 * @property applicationScope The coroutine scope used for launching coroutines.
 */
class ChirpFirebaseMessagingService : FirebaseMessagingService() {

    private val deviceTokenService by inject<DeviceTokenService>()
    private val sessionStorage by inject<SessionStorage>()
    private val applicationScope by inject<CoroutineScope>()

    /**
     * Called when a new Firebase Cloud Messaging (FCM) token is generated.
     * This method handles the registration of the new token with the device token service
     * if the user is authenticated based on observed session information.
     *
     * @param token The newly generated FCM token.
     */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        applicationScope.launch {
            val authInfo = sessionStorage.observeAuthInfo().first()
            if (authInfo != null) {
                deviceTokenService.registerToken(
                    token = token,
                    platform = "ANDROID"
                )
            }
        }
    }
}