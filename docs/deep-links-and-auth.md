# Authentication & Deep Links

This document summarizes authentication flows and deep link handling across platforms.

## Flows

- Register → Register Success → Email Verification (deep link)
- Login (session persistence via DataStore)
- Forgot Password → Reset Password (deep link)
- On successful login, navigate to `ChatListRoute` and clear auth back stack

Deep links handled in `authGraph`:

- Verify: `https://chirp.pl-coding.com/api/auth/verify?token={token}` and
  `chirp://chirp.pl-coding.com/api/auth/verify?token={token}`
- Reset: `https://chirp.pl-coding.com/api/auth/reset-password?token={token}` and
  `chirp://chirp.pl-coding.com/api/auth/reset-password?token={token}`

Platform setup:

- Android: `MainActivity` intent filters for HTTPS (App Links, `android:autoVerify="true"`) and
  `chirp://` scheme; host `chirp.pl-coding.com`, paths `/api/auth/verify` and
  `/api/auth/reset-password`.
- iOS: custom URL scheme `chirp` in Info.plist; Universal Links for HTTPS are TODO (Associated
  Domains + AASA).

References:

- `feature/auth/presentation/.../AuthGraph.kt`
- `composeApp/.../NavigationRoot.kt`
- `CHANGELOG.md`

## Auth Flow Diagram

```mermaid
stateDiagram-v2
  [*] --> Login
  Login --> Register: "Create account"
  Register --> RegisterSuccess: "Submitted"
  RegisterSuccess --> EmailVerificationPending: "Await email"

  EmailVerificationPending --> Login: "Open verify link (deep link)\n-> verified server-side"

  Login --> ChatList: "Login success\n(clear auth back stack)"

  Login --> ForgotPassword: "Forgot password"
  ForgotPassword --> ResetPasswordPending: "Email sent"
  ResetPasswordPending --> Login: "Open reset link (deep link)\n-> set new password"

  ChatList --> [*]

  state ChatList {
    [*] --> ChatListScreen
    ChatListScreen --> [*]
  }

  note right of Login
    Session persisted in DataStore
    with auto refresh/expiration.
  end note

  ChatList --> Login: "Logout / Session expired\n(clear to auth graph)"
```

## Deep Link Routing Diagram

```mermaid
flowchart LR
  subgraph URLs
    A1["https://chirp.pl-coding.com/api/auth/verify?token={t}"]
    A2["chirp://chirp.pl-coding.com/api/auth/verify?token={t}"]
    B1["https://chirp.pl-coding.com/api/auth/reset-password?token={t}"]
    B2["chirp://chirp.pl-coding.com/api/auth/reset-password?token={t}"]
  end

  subgraph Platform_handlers
    H1["Android Intent Filters\n(MainActivity)"]
    H2["iOS URL scheme 'chirp'\n(+ Universal Links TODO)"]
  end

  subgraph App_routing_authGraph
    R1["VerifyEmailDestination"]
    R2["ResetPasswordDestination"]
  end

  URLs --> H1
  URLs --> H2
  H1 --> R1
  H1 --> R2
  H2 --> R1
  H2 --> R2

  R1 --> Login
  R2 --> ResetPasswordScreen --> Login
```

Notes:

- After verification/reset via deep link, route back to `Login` (or auto-navigate to `ChatList` when
  a valid session is present).
- Ensure tokens are consumed server-side; the app treats the link as an entry point and updates UI
  state accordingly.
