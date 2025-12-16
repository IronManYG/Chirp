# Configuration

This document covers environment variables, secrets, and platform-specific configuration required to
build and run Chirp.

## Table of Contents

- [Overview](#overview)
- [BuildKonfig](#buildkonfig)
- [Firebase Setup](#firebase-setup)
- [Room Schemas](#room-schemas)
- [Desktop Deep Links](#desktop-deep-links)
- [Android Release Signing](#android-release-signing)

## Overview

No global environment variables are required for a basic build. Platform-specific configuration is
handled through local files that are git-ignored.

## BuildKonfig

Some modules apply the `dev.gaddal.convention.buildkonfig` convention plugin. When applied, you *
*must** define `API_KEY` in `local.properties` at the repository root, or the build will fail.

### Setup

Add the following to `local.properties` (do not commit this file):

```properties
API_KEY=your_value_here
```

### Modules Using BuildKonfig

| Module              | Description                       |
|---------------------|-----------------------------------|
| `core:data`         | Core data layer API configuration |
| `feature:chat:data` | Chat feature API configuration    |

> **Note:** The generated package name for BuildKonfig is derived from the module path via
`pathToPackageName()`.

## Firebase Setup

### Android (FCM)

The Android app applies `com.google.gms.google-services`. You need to provide your Firebase
configuration:

1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app with package name `dev.gaddal.chirp`
3. Download `google-services.json`
4. Place it in `composeApp/google-services.json`

> **Important:** This file is git-ignored. Do not commit Firebase credentials.

### iOS (Firebase)

For iOS push notifications:

1. Configure APNs certificates/keys in your Apple Developer account
2. Add Firebase iOS app in Firebase Console
3. Download `GoogleService-Info.plist`
4. Place it in the iOS target (under `iosApp` as required by your Xcode setup)
5. Enable capabilities in Xcode:
    - Push Notifications
    - Background Modes → Remote notifications

> **TODO:** Document exact file path and capabilities once finalized.

## Room Schemas

The Room convention plugin configures `schemaDirectory` to `<module>/schemas`.

### Current Schema Locations

| Module                  | Schema Path                     |
|-------------------------|---------------------------------|
| `feature/chat/database` | `feature/chat/database/schemas` |

### Best Practices

- **Commit schemas** to version control for reliable migration testing
- Schema files are auto-generated when Room entities change
- Review schema changes in PRs to catch unintended database modifications

## Desktop Deep Links

The app registers the custom URL scheme `chirp` for deep linking on desktop via Conveyor
configuration.

### Configuration in `conveyor.conf`

```hocon
url-schemes = ["chirp"]
```

### Windows App URI Handler

Windows App URI handler is configured for `chirp.pl-coding.com`:

```hocon
// In conveyor.conf
app {
  windows {
    // App URI handler configuration
  }
}
```

## Android Release Signing

For release builds, configure signing locally:

1. Create or use an existing keystore
2. Add signing configuration to `local.properties`:

```properties
RELEASE_STORE_FILE=path/to/keystore.jks
RELEASE_STORE_PASSWORD=your_store_password
RELEASE_KEY_ALIAS=your_key_alias
RELEASE_KEY_PASSWORD=your_key_password
```

> **Important:** Never commit keystores or signing credentials to version control.

### Build Release APK

```bash
# Windows
.\gradlew.bat :composeApp:assembleRelease

# macOS/Linux
./gradlew :composeApp:assembleRelease
```

### Build Release Bundle (AAB)

```bash
# Windows
.\gradlew.bat :composeApp:bundleRelease

# macOS/Linux
./gradlew :composeApp:bundleRelease
```

## Potential Future Configuration

| Configuration | Purpose                   | Status          |
|---------------|---------------------------|-----------------|
| API endpoints | Ktor client configuration | Use BuildKonfig |
| Feature flags | Runtime feature toggles   | Not implemented |
| Analytics     | Usage tracking            | Not implemented |

> **Security Note:** Use BuildKonfig or another secure mechanism for non-secret config. Avoid
> committing secrets to version control.
