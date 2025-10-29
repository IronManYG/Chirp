package dev.gaddal.chirp

/**
 * Represents the state of the main application, including user authentication status
 * and the process of authentication checking.
 *
 * This class is primarily used to manage and observe the authentication flow and state transitions
 * within the `MainViewModel`. It acts as a container for holding relevant state properties
 * such as whether the user is logged in or if the authentication check is in progress.
 *
 * @property isLoggedIn Indicates whether the user is currently authenticated and logged in.
 * Defaults to `false`, meaning the user is not logged in.
 *
 * @property isCheckingAuth Indicates whether the application is currently performing an authentication check.
 * Defaults to `true`, meaning authentication is being verified.
 */
data class MainState(
    val isLoggedIn: Boolean = false,
    val isCheckingAuth: Boolean = true
)