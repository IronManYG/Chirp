package dev.gaddal.chirp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dev.gaddal.auth.presentation.navigation.AuthGraphRoutes
import dev.gaddal.auth.presentation.navigation.authGraph
import dev.gaddal.chat.presentation.chat_list.ChatListRoute
import dev.gaddal.chat.presentation.chat_list.ChatListScreenRoot

/**
 * Represents the composable root for the navigation graph in the application.
 *
 * This function initializes a [NavHost] with the provided [navController] as the navigation controller.
 * It serves as the entry point for the application's navigation system, defining the navigation flow
 * starting from the provided [startDestination].
 *
 * The [authGraph] extension is used to set up all authentication-related navigation destinations,
 * including language selection, registration, login, and email verification flows. Deep linking support
 * is configured within the auth graph for handling external navigation requests.
 *
 * After successful login, the user is navigated to the chat list screen with the auth graph cleared
 * from the back stack.
 *
 * @param navController The navigation controller that manages the app navigation.
 * @param startDestination The initial route that serves as the start destination.
 * @param startAtLanguageSelection Whether to start at language selection screen instead of login.
 * @see AuthGraphRoutes
 * @see authGraph
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
                navController.navigate(ChatListRoute) {
                    popUpTo(AuthGraphRoutes.Graph) {
                        inclusive = true
                    }
                }
            },
            startAtLanguageSelection = startAtLanguageSelection
        )
        composable<ChatListRoute> {
            ChatListScreenRoot()
        }
    }
}