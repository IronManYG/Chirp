package dev.gaddal.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import dev.gaddal.auth.presentation.navigation.AuthGraphRoutes
import dev.gaddal.auth.presentation.navigation.authGraph
import dev.gaddal.chat.presentation.navigation.ChatGraphRoutes
import dev.gaddal.chat.presentation.navigation.chatGraph

/**
 * Represents the composable root for the navigation graph in the application.
 *
 * This function initializes a [NavHost] with the provided [navController] as the navigation controller.
 * It serves as the entry point for the application's navigation system, defining the navigation flow
 * starting from the provided [startDestination].
 *
 * The navigation structure consists of two main graphs:
 * - [authGraph]: Sets up all authentication-related navigation destinations, including language
 *   selection, registration, login, and email verification flows. Deep linking support is configured
 *   within this graph for handling external navigation requests.
 * - [chatGraph]: Manages chat-related navigation, including the chat list and detail views using
 *   an adaptive layout system.
 *
 *
 * After successful login, the user is navigated to the chat list screen with the auth graph cleared
 * from the back stack.
 *
 * @param navController The navigation controller that manages the app navigation.
 * @param startDestination The initial route that serves as the start destination.
 * @param startAtLanguageSelection Whether to start at language selection screen instead of login.
 * @see AuthGraphRoutes
 * @see ChatGraphRoutes
 * @see authGraph
 * @see chatGraph
 */
@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: Any,
    startAtLanguageSelection: Boolean = false
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(ChatGraphRoutes.Graph) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            },
            startAtLanguageSelection = startAtLanguageSelection
        )
        chatGraph(
            navController = navController,
            onLogout = {
                navController.navigate(AuthGraphRoutes.Graph) {
                    popUpTo(ChatGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            }
        )
    }
}