package dev.gaddal.chat.presentation.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import dev.gaddal.core.designsystem.theme.extended

/**
 * Determines the chat bubble color for a specified user based on their user ID.
 *
 * @param userId The unique identifier of the user.
 * @return A [Color] object representing the assigned color for the user's chat bubble.
 */
@Composable
fun getChatBubbleColorForUser(userId: String): Color {
    val colorPool = with(MaterialTheme.colorScheme.extended) {
        listOf(
            cakeViolet,
            cakeGreen,
            cakePink,
            cakeOrange,
            cakeBlue,
            cakeYellow,
            cakePurple,
            cakeRed,
            cakeMint,
        )
    }
    val index = userId.hashCode().toUInt() % colorPool.size.toUInt()

    return colorPool[index.toInt()]
}