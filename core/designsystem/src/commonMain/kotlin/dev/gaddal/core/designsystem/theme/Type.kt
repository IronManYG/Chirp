package dev.gaddal.core.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import chirp.core.designsystem.generated.resources.Res
import chirp.core.designsystem.generated.resources.cairo_light
import chirp.core.designsystem.generated.resources.cairo_regular
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

val Cairo @Composable get() = FontFamily(
    Font(
        resource = Res.font.cairo_light,
        weight = FontWeight.Light
    ),
    Font(
        resource = Res.font.cairo_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resource = Res.font.cairo_regular,
        weight = FontWeight.Medium
    ),
    Font(
        resource = Res.font.cairo_regular,
        weight = FontWeight.SemiBold
    ),
    Font(
        resource = Res.font.cairo_regular,
        weight = FontWeight.Bold
    ),
)

private fun isArabicLike(code: String): Boolean {
    val primary = code.trim().lowercase().substringBefore('-')
    return primary in setOf("ar", "fa", "he", "ur")
}

@Composable
private fun buildTypography(baseFamily: FontFamily): Typography = Typography(
    titleLarge = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 36.sp
    ),
    titleMedium = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleSmall = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 26.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodySmall = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    ),
    displaySmall = TextStyle(
        fontFamily = baseFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp
    ),
)

/**
 * Represents a text style for extra small labels, designed for displaying secondary or auxiliary text.
 *
 * Uses the currently provided MaterialTheme.typography font family when available to align with
 * the active language selection; falls back to PlusJakartaSans otherwise.
 */
val Typography.labelXSmall: TextStyle
    @Composable get() = TextStyle(
        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily ?: PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )

/**
 * Represents the smallest title text style within the typography system.
 * Aligns font family with the active theme's typography.
 */
val Typography.titleXSmall: TextStyle
    @Composable get() = TextStyle(
        fontFamily = MaterialTheme.typography.bodyMedium.fontFamily ?: PlusJakartaSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )

/**
 * Default typography used across the application with PlusJakartaSans.
 */
val Typography @Composable get() = buildTypography(PlusJakartaSans)

/**
 * Build a typography set that matches the provided [languageCode].
 *
 * Cairo is used for Arabic-like languages (ar, fa, he, ur); PlusJakartaSans otherwise.
 */
@Composable
fun typographyForLanguage(languageCode: String): Typography {
    val family = if (isArabicLike(languageCode)) Cairo else PlusJakartaSans
    return buildTypography(family)
}