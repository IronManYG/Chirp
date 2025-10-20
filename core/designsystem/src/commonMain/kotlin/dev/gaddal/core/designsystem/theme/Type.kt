package dev.gaddal.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.plusjakartasans_bold
import chirp.core.designsystem.generated.resources.plusjakartasans_light
import chirp.core.designsystem.generated.resources.plusjakartasans_medium
import chirp.core.designsystem.generated.resources.plusjakartasans_regular
import chirp.core.designsystem.generated.resources.plusjakartasans_semibold
import org.jetbrains.compose.resources.Font

/**
 * A [FontFamily] object representing the Plus Jakarta Sans typeface, comprising multiple font weights.
 *
 * The available font weights include:
 * - Light
 * - Normal
 * - Medium
 * - SemiBold
 * - Bold
 *
 * Each font weight is linked to its corresponding resource, enabling the use of consistent typography
 * throughout the application.
 */
val PlusJakartaSans @Composable get() = FontFamily(
    Font(
        resource = Res.font.plusjakartasans_light,
        weight = FontWeight.Light
    ),
    Font(
        resource = Res.font.plusjakartasans_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resource = Res.font.plusjakartasans_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resource = Res.font.plusjakartasans_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resource = Res.font.plusjakartasans_bold,
        weight = FontWeight.Bold
    ),
)

/**
 * Represents a text style for extra small labels, designed for displaying secondary or auxiliary text.
 *
 * The style uses the "Plus Jakarta Sans" font family with a semi-bold weight, making the text slightly emphasized.
 * Font size is set to 11 sp, which is optimal for compact text requirements, while the line height is 14 sp to maintain readability.
 */
val Typography.labelXSmall: TextStyle
    @Composable get() = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )

/**
 * Represents the smallest title text style within the typography system.
 * titleXSmall is styled with a semi-bold weight, a font size of 14sp,
 * and a line height of 18sp. It uses the PlusJakartaSans font family.
 * This style is commonly used for small headings or titles requiring subtle emphasis.
 */
val Typography.titleXSmall: TextStyle
    @Composable get() = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

/**
 * Customizes the typography styles used across the application using the PlusJakartaSans font family.
 * Provides a set of predefined text styles for titles, body text, labels, headlines, and displays
 * with specific font weights, sizes, and line heights.
 */
val Typography @Composable get() = Typography(
    titleLarge = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 36.sp
    ),
    titleMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleSmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    displaySmall = TextStyle(
        fontFamily = PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp
    ),
)