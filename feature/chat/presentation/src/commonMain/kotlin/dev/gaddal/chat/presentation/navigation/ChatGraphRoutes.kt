package dev.gaddal.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
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
     * Represents a navigation route specific to the chat list detail screen
     * within the ChatGraphRoutes.
     *
     * This object is used as a destination identifier within the chat
     * navigation graph. It is primarily utilized to navigate to the
     * detail view of a chat list, where users can interact with the
     * chat items and view or manage them.
     *
     * As part of the ChatGraphRoutes, this route is integrated into the
     * larger navigation structure of the chat feature.
     */
    @Serializable
    data object ChatListDetailRoute : ChatGraphRoutes
}

/**
 * Defines the navigation graph for chat-related screens and manages the respective destinations within it.
 *
 * This function adds navigation routes for the chat list and detail screens to the navigation graph.
 * It utilizes an adaptive layout system to provide an optimized UI experience for different device types and orientations.
 *
 * @param navController The navigation controller used to manage navigation operations between the chat-related screens.
 */
fun NavGraphBuilder.chatGraph(
    navController: NavController
) {
    navigation<ChatGraphRoutes.Graph>(
        startDestination = ChatGraphRoutes.ChatListDetailRoute
    ) {
        composable<ChatGraphRoutes.ChatListDetailRoute> {
            ChatListDetailAdaptiveLayout()
        }
    }
}