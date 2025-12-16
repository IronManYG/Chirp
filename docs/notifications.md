# Push Notifications

Chirp uses Firebase for push notifications on Android and iOS.

## Android (FCM)

- Ensure `composeApp/google-services.json` is present for your Firebase project (this file is
  git-ignored).
- On Android 13+ (API 33+), the app requests the notification permission at runtime.
- Relevant code:
    - `feature/chat/data/.../ChirpFirebaseMessagingService.kt`
    - Device token handling and backend registration in KMP (e.g.,
      `feature/chat/data/.../KtorDeviceTokenService.kt`).

## iOS (Firebase)

- Provide `GoogleService-Info.plist` and configure APNs certificates/keys in your Apple developer
  account.
- Enable capabilities in Xcode: Push Notifications + Background Modes (Remote notifications).
- Universal Links/AASA are not required for push, but may be needed for deep links.

## Backend Considerations

- Ensure device tokens are registered/updated server-side.
- Tokens should be invalidated/rotated as required by platform and Firebase guidelines.
