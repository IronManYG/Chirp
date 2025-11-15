package dev.gaddal.core.designsystem.components.dropdown

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Represents an item to be displayed in a dropdown menu.
 *
 * @property title The title of the dropdown item. This is typically the text that describes the action or option.
 * @property icon The icon associated with the dropdown item. This provides a visual representation of the item's purpose.
 * @property contentColor The color used for rendering the item's content. This can help in differentiating items visually.
 * @property onClick A lambda function to be executed when the dropdown item is clicked. Defines the action associated with this item.
 */
data class DropDownItem(
    val title: String,
    val icon: ImageVector,
    val contentColor: Color,
    val onClick: () -> Unit
)