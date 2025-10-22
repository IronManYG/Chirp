package dev.gaddal.core.presentation.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

/**
 * Represents a text container used for handling both dynamic strings and
 * string resources with optional formatting arguments.
 */
sealed interface UiText {
    /**
     * Represents a UI text element that holds a dynamic, string-based value.
     * Used to directly display or handle strings in the UI without needing localized resources.
     *
     * @property value The string content to be rendered or utilized as dynamic text.
     */
    data class DynamicString(val value: String): UiText
    /**
     * Represents a text resource that can dynamically include arguments for localization.
     *
     * This class is an implementation of the `UiText` interface and is used to provide
     * localized strings with optional arguments. It wraps a `StringResource` ID and an array
     * of arguments that can be used to format the resource string at runtime.
     *
     * @property id The resource ID of the string to be localized. It corresponds to a `StringResource`.
     * @property args An optional array of arguments to be used for string formatting. Defaults to an empty array.
     */
    class Resource(
        val id: StringResource,
        val args: Array<Any> = arrayOf()
    ): UiText

    /**
     * Converts the current [UiText] instance to a string representation.
     *
     * For a [DynamicString], it directly returns the encapsulated string value.
     * For a [Resource], it retrieves the string resource using `stringResource`
     * with the provided resource ID and arguments.
     *
     * @return The string representation of the [UiText] instance.
     */
    @Composable
    fun asString(): String {
        return when(this) {
            is DynamicString -> value
            is Resource -> stringResource(
                resource = id,
                *args
            )
        }
    }

    /**
     * Converts the current `UiText` instance to a `String` asynchronously.
     *
     * This method handles both `DynamicString` and `Resource` implementations of `UiText`.
     * For `DynamicString`, it directly returns the string value.
     * For `Resource`, it resolves the string using the provided resource ID and arguments.
     *
     * @return The string representation of the `UiText` after resolving its content.
     */
    suspend fun asStringAsync(): String {
        return when(this) {
            is DynamicString -> value
            is Resource -> getString(
                resource = id,
                *args
            )
        }
    }
}