package dev.gaddal.auth.presentation.navigation

import kotlinx.serialization.Serializable

/**
 * Represents the set of routes available in the authentication navigation graph.
 * This interface is used to define all navigation destinations related to authentication.
 */
sealed interface AuthGraphRoutes {
    /**
     * Represents the entry point of the authentication navigation graph.
     *
     * This object serves as the root for defining all navigation routes
     * related to the authentication flow. It is used as the parent
     * navigation graph within `authGraph` function to organize and encapsulate
     * authentication-related destinations.
     */
    @Serializable
    data object Graph : AuthGraphRoutes

    /**
     * First-run language selection entry within the authentication graph.
     * Navigate here when the user has not chosen a language yet.
     */
    @Serializable
    data object LanguageSelection : AuthGraphRoutes

    /**
     * Represents the route for the login feature in the authentication navigation graph.
     * This route is used to navigate to the login screen within the application's authentication flow.
     */
    @Serializable
    data object Login : AuthGraphRoutes

    /**
     * Represents the registration entry point within the authentication navigation graph.
     *
     * This object is used as the starting destination for the authentication flow,
     * allowing users to access the registration screen. It implements the `AuthGraphRoutes`
     * interface as part of the navigation structure for handling user authentication workflows.
     */
    @Serializable
    data object Register : AuthGraphRoutes

    /**
     * Represents the successful registration state in the authentication navigation graph.
     *
     * @property email The email address of the user who has successfully registered.
     */
    @Serializable
    data class RegisterSuccess(val email: String) : AuthGraphRoutes

    /**
     * Represents a navigation route in the authentication flow for the "Forgot Password" feature.
     * Used to initiate the forgot password process and navigate to the relevant screen in the app's navigation graph.
     * Implements the AuthGraphRoutes interface to be part of the authentication navigation structure.
     */
    @Serializable
    data object ForgotPassword : AuthGraphRoutes

    /**
     * Represents a navigation route for resetting a user's password.
     * Part of the authentication graph navigation structure, designed to
     * handle user actions related to password resetting within the app.
     *
     * @property token The token used to authenticate and validate the password reset request.
     */
    @Serializable
    data class ResetPassword(val token: String) : AuthGraphRoutes

    /**
     * Represents the email verification route within the authentication navigation graph.
     *
     * This class holds the verification token which is used to validate the user's email address
     * during the registration or authentication processes.
     *
     * Implements the AuthGraphRoutes interface to integrate with the broader authentication
     * navigation system.
     */
    @Serializable
    data class EmailVerification(val token: String) : AuthGraphRoutes
}