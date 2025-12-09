package dev.gaddal.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import androidx.navigation.toRoute
import dev.gaddal.chat.presentation.chat_list_detail.ChatListDetailAdaptiveLayout
import kotlinx.serialization.Serializable

/**
 * Defines the navigation routes for the chat feature within the application.
 *
 * This sealed interface represents the distinct routes that are part of the chat graph.
 * Each implementation corresponds to a specific destination in the chat navigation mechanism.
 * It enables type-safe navigation by associating specific destinations with structured route objects.
 *
 * The chat graph manages chat-related navigation, including the chat list and chat detail views.
 */
sealed interface ChatGraphRoutes {
    /**
     * Represents the root of the chat-related navigation graph in the application.
     *
     * This object serves as the primary entry point for the chat graph navigation in the app's navigation system.
     * It is used as the `startDestination` for navigating to chat-related screens. The chat graph
     * encompasses destinations such as chat list and chat detail views, enabling modular navigation configurations.
     *
     * This object implements the `ChatGraphRoutes` sealed interface, providing a type-safe way to define routing
     * logic within the navigation graph.
     *
     * Usage of this object ensures consistent and centralized routing for all chat-related flows in the app,
     * allowing flexibility in the implementation and control of navigation structures.
     *
     * Key Features:
     * - Acts as an identifier for the starting point in the chat-related navigation graph.
     * - Part of the sealed hierarchy of `ChatGraphRoutes`, ensuring extensibility and type safety.
     *
     * See Also:
     * - [ChatGraphRoutes]: The sealed interface for chat graph routes.
     * - [NavGraphBuilder.chatGraph]: The function defining the structure of the chat graph.
     */
    @Serializable
    data object Graph : ChatGraphRoutes

    /**
     * Represents the navigation route to the main chat screen, which adaptively displays
     * both the chat list and chat detail views.
     *
     * This route is the primary destination within the chat graph. It can optionally accept a
     * `chatId` to directly open a specific conversation. If `chatId` is `null`, it displays the
     * chat list. On larger screens, it may show the chat list and a selected chat detail side-by-side.
     *
     * This class is part of the `ChatGraphRoutes` sealed interface, ensuring type-safe navigation
     * for chat-related features.
     *
     * @property chatId An optional ID of the chat to be displayed. If `null`, no specific chat is
     *           pre-selected, and the UI will typically show the chat list.
     */
    @Serializable
    data class ChatListDetailRoute(val chatId: String? = null) : ChatGraphRoutes
}

/**
 * Defines the nested navigation graph for the chat feature.
 *
 * This extension function on `NavGraphBuilder` sets up the chat-related navigation,
 * including routes for chat list and detail screens. It uses a `navigation` block
 * to group these routes under a common graph, identified by `ChatGraphRoutes.Graph`.
 *
 * The graph's starting destination is `ChatListDetailRoute`, which displays an adaptive
 * layout for both the chat list and detail views. This adaptive layout, `ChatListDetailAdaptiveLayout`,
 * is capable of showing a list-detail interface on larger screens or handling navigation between
 * list and detail on smaller screens.
 *
 * It also configures a deep link to allow external navigation directly to a specific chat
 * via a URI like `chirp://chat_detail/{chatId}`.
 *
 * @param navController The `NavController` for handling navigation actions within the graph,
 *                      such as navigating to different chats or handling back navigation.
 * @param onLogout The callback function to be invoked when the user initiates a logout action.
 */
fun NavGraphBuilder.chatGraph(
    navController: NavController,
    onLogout: () -> Unit
) {
    navigation<ChatGraphRoutes.Graph>(
        startDestination = ChatGraphRoutes.ChatListDetailRoute(null)
    ) {
        composable<ChatGraphRoutes.ChatListDetailRoute>(
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "chirp://chat_detail/{chatId}"
                }
            )
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<ChatGraphRoutes.ChatListDetailRoute>()
            ChatListDetailAdaptiveLayout(
                initialChatId = route.chatId,
                onLogout = onLogout
            )
        }
    }
}