package dev.gaddal.core.designsystem.components.chat

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

/**
 * Represents the position of a triangle shape used in certain UI components, such as the chat bubble.
 * It determines the alignment of the triangle, which can be positioned on the left or right side.
 */
enum class TrianglePosition {
    LEFT,
    RIGHT
}

/**
 * A custom shape class used to create chat bubble shapes with a triangular pointer indicating
 * the direction of the message (left or right).
 *
 * @param trianglePosition The position of the triangular pointer, indicating the side of the bubble.
 *                         It can either be [TrianglePosition.LEFT] or [TrianglePosition.RIGHT].
 * @param triangleSize The size of the triangular pointer in Dp. This value determines the width
 *                     of the triangle and its contribution to the bubble's shape.
 *                     Default value is 16.dp.
 * @param cornerRadius The radius for rounding the corners of the chat bubble. Affects the
 *                     overall curvature of the chat bubble's rectangular body. Default value is 8.dp.
 */
class ChatBubbleShape(
    private val trianglePosition: TrianglePosition,
    private val triangleSize: Dp = 16.dp,
    private val cornerRadius: Dp = 8.dp
): Shape {

    /**
     * Creates an outline for a custom chat bubble shape, including a triangular pointer
     * on the left or right side depending on the specified position.
     *
     * @param size The dimensions of the layout or widget to draw the shape on.
     * @param layoutDirection The layout direction of the current environment (e.g., RTL or LTR).
     * @param density A helper object to convert dimension units and access display density.
     * @return The `Outline` that defines the path for the custom bubble shape.
     */
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val triangleSizePx = with(density) { triangleSize.toPx() }
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }

        // Respect layout direction: mirror LEFT/RIGHT for RTL so callers can pass logical sides.
        val effectiveTrianglePosition = when (layoutDirection) {
            LayoutDirection.Ltr -> trianglePosition
            LayoutDirection.Rtl -> when (trianglePosition) {
                TrianglePosition.LEFT -> TrianglePosition.RIGHT
                TrianglePosition.RIGHT -> TrianglePosition.LEFT
            }
        }

        val path = when (effectiveTrianglePosition) {
            TrianglePosition.LEFT -> {
                val bodyPath = Path().apply {
                    addRoundRect(
                        roundRect = RoundRect(
                            left = triangleSizePx,
                            top = 0f,
                            right = size.width,
                            bottom = size.height,
                            cornerRadius = CornerRadius(
                                x = cornerRadiusPx,
                                y = cornerRadiusPx
                            )
                        )
                    )
                }
                val trianglePath = Path().apply {
                    moveTo(0f, size.height)
                    lineTo(triangleSizePx, size.height - cornerRadiusPx)
                    lineTo(triangleSizePx + cornerRadiusPx, size.height)
                    close()
                }

                Path.combine(PathOperation.Union, bodyPath, trianglePath)
            }
            TrianglePosition.RIGHT -> {
                val bodyPath = Path().apply {
                    addRoundRect(
                        roundRect = RoundRect(
                            left = 0f,
                            top = 0f,
                            right = size.width - triangleSizePx,
                            bottom = size.height,
                            cornerRadius = CornerRadius(
                                x = cornerRadiusPx,
                                y = cornerRadiusPx
                            )
                        )
                    )
                }
                val trianglePath = Path().apply {
                    moveTo(size.width, size.height)
                    lineTo(size.width - triangleSizePx, size.height - cornerRadiusPx)
                    lineTo(size.width - triangleSizePx - cornerRadiusPx, size.height)
                    close()
                }
                Path.combine(PathOperation.Union, bodyPath, trianglePath)
            }
        }

        return Outline.Generic(path)
    }
}