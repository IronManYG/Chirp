// AppDelegate.swift
// Sets up Firebase, registers for and handles push notifications (APNs/FCM),
// forwards deep links from notification taps into the app, and manages the FCM token.

import Foundation
import ComposeApp
import UIKit
import UserNotifications
import FirebaseCore
import FirebaseMessaging

/// AppDelegate acts as the central entry point for app lifecycle and notification events.
/// Responsibilities:
/// - Configure Firebase on launch
/// - Register as the delegate for UNUserNotificationCenter and Firebase Messaging
/// - Maintain and refresh the FCM registration token
/// - Handle incoming remote notifications (foreground/background)
/// - Route notification taps to in-app deep links
class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {

    /// Called when the app has finished launching.
    /// - Configures Firebase.
    /// - Sets delegates for user notifications and Firebase Messaging so we can receive tokens and notifications.
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]?) -> Bool {
        FirebaseApp.configure()

        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self

        return true
    }

    /// Called after APNs registration succeeds with the device token.
    /// - Assigns the APNs token to Firebase Messaging so FCM can send notifications to this device.
    /// - Triggers a token refresh to ensure we have the latest FCM token linked to this APNs token.
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken

        refreshToken()
    }

    /// Called when APNs registration fails.
    /// - Logs the error for diagnostics. Consider surfacing this in debug UI if needed.
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
        print("iOS: Failed to register for push notifications: \(error.localizedDescription)")
    }

    /// Firebase Messaging delegate callback with the current FCM registration token.
    /// - Stores the token locally and forwards it to the shared bridge for use by the Kotlin/Compose layer.
    /// - If the token is empty or missing, attempts to refresh it.
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        guard let token = fcmToken, !token.isEmpty else { // Token may be nil on first launch or after invalidation; request a fresh one.
            refreshToken()
            return
        }

        print("iOS: FCM registration token (didReceiveRegistrationToken): \(token)")

        UserDefaults.standard.set(token, forKey: "FCM_TOKEN")
        IosDeviceTokenHolderBridge.shared.updateToken(token: token)
    }

    /// Handles incoming remote notifications (silent or with content) when received in the background/foreground with fetch.
    /// - Forwards the payload to Firebase Messaging for analytics and message handling, then calls the fetch completion handler.
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable: Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        Messaging.messaging().appDidReceiveMessage(userInfo)
        completionHandler(.newData)
    }

    /// Controls how notifications are displayed when the app is in the foreground.
    /// - Currently shows a banner. Adjust options as needed (e.g., .sound, .badge).
    func userNotificationCenter(_ center: UNUserNotificationCenter, willPresent notification: UNNotification, withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        completionHandler([.banner])
    }

    /// Handles user interactions with delivered notifications (e.g., tapping a banner or action).
    /// - Extracts a `chatId` from the payload (if present) and constructs an in-app deep link.
    /// - Forwards the deep link to `ExternalUriHandler` to navigate to the target screen.
    func userNotificationCenter(_ center: UNUserNotificationCenter, didReceive response: UNNotificationResponse, withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo

        // Expect a custom payload key "chatId" to deep link into a specific chat.
        if let chatId = userInfo["chatId"] as? String {
            let deepLinkUrl = "chirp://chat_detail/\(chatId)"
            ExternalUriHandler.shared.onNewUri(uri: deepLinkUrl)
        }

        completionHandler()
    }

    /// Requests/refreshes the FCM token asynchronously and updates shared state.
    /// - Saves the token to UserDefaults and informs the bridge so the shared code can use it.
    /// - Logs an error if token retrieval fails.
    func refreshToken() {
        Task {
            do {
                let fcmToken = try await Messaging.messaging().token()

                UserDefaults.standard.set(fcmToken, forKey: "FCM_TOKEN")
                IosDeviceTokenHolderBridge.shared.updateToken(token: fcmToken)
            } catch {
                print("iOS: Error getting FCM token: \(error.localizedDescription)")
            }
        }
    }
}

