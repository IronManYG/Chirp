package dev.gaddal.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * A CompositionLocal that provides access to the `ExtendedColors` used throughout the design system.
 *
 * `ExtendedColors` encapsulate additional color definitions beyond the standard Material Design color palette,
 * enabling a richer and more customized design language. The default value is set to `LightExtendedColors`,
 * representing the light theme colors.
 *
 * It can be overridden within a `CompositionLocalProvider` to apply custom or dark theme color definitions
 * where needed.
 */
val LocalExtendedColors = staticCompositionLocalOf { LightExtendedColors }

/**
 * Provides access to the application's extended color palette, defined by the [ExtendedColors] object.
 *
 * This property is an extension on [ColorScheme] and includes additional custom colors that
 * complement the Material Design color system. The extended color palette includes options for
 * button states, text variants, surface variations, accent colors, and chat bubble colors.
 *
 * The colors are retrieved from the current composition using [LocalExtendedColors].
 */
val ColorScheme.extended: ExtendedColors
    @ReadOnlyComposable
    @Composable
    get() = LocalExtendedColors.current

/**
 * A data class that defines an extended color palette used for various UI elements in the application.
 * The colors are categorized into button states, text variants, surface variants, accent colors,
 * and specific colors for chat bubble themes.
 *
 * Button states:
 * - Colors representing hover states, disabled states, success, and outlines for buttons.
 *
 * Text variants:
 * - Different text colors used in the application, such as primary, secondary, tertiary, placeholder, and disabled.
 *
 * Surface variants:
 * - Colors for surfaces, overlays, and outlines, allowing customization of the UI depth and layers.
 *
 * Accent colors:
 * - A collection of predefined unique colors (e.g., blue, purple, pink, green) used to highlight elements or provide visual accents.
 *
 * Chat bubble colors:
 * - A set of colors specifically for chat themes, represented by vibrant or muted tones (e.g., violet, green, mint, red).
 *
 * This class is designed to enhance the consistency and flexibility of the UI by offering a comprehensive set of colors
 * that can be used across various components and themes.
 *
 * @property primaryHover The hover color for primary buttons.
 * @property destructiveHover The hover color for destructive actions.
 * @property destructiveSecondaryOutline The outline color for secondary destructive actions.
 * @property disabledOutline The outline color for disabled components.
 * @property disabledFill The fill color for disabled components.
 * @property successOutline The outline color for success states.
 * @property success The primary color for success indications.
 * @property onSuccess The text color for content displayed on success backgrounds.
 * @property secondaryFill The fill color for secondary components or backgrounds.
 * @property textPrimary The primary color for text.
 * @property textTertiary The tertiary or less prominent color for text.
 * @property textSecondary The secondary color for text.
 * @property textPlaceholder The color for placeholder text or hints.
 * @property textDisabled The color for disabled text elements.
 * @property surfaceLower The lower elevation surface color.
 * @property surfaceHigher The higher elevation surface color.
 * @property surfaceOutline The outline color for surface variants.
 * @property overlay The overlay color for modals or semi-transparent surfaces.
 * @property accentBlue A blue accent color used for highlights or decorations.
 * @property accentPurple A purple accent color used for highlights or decorations.
 * @property accentViolet A violet accent color used for highlights or decorations.
 * @property accentPink A pink accent color used for highlights or decorations.
 * @property accentOrange An orange accent color used for highlights or decorations.
 * @property accentYellow A yellow accent color used for highlights or decorations.
 * @property accentGreen A green accent color used for highlights or decorations.
 * @property accentTeal A teal accent color used for highlights or decorations.
 * @property accentLightBlue A light blue accent color used for highlights or decorations.
 * @property accentGrey A grey accent color used for highlights or decorations.
 * @property cakeViolet A violet color for chat bubble themes.
 * @property cakeGreen A green color for chat bubble themes.
 * @property cakeBlue A blue color for chat bubble themes.
 * @property cakePink A pink color for chat bubble themes.
 * @property cakeOrange An orange color for chat bubble themes.
 * @property cakeYellow A yellow color for chat bubble themes.
 * @property cakeTeal A teal color for chat bubble themes.
 * @property cakePurple A purple color for chat bubble themes.
 * @property cakeRed A red color for chat bubble themes.
 * @property cakeMint A mint color for chat bubble themes.
 */
@Immutable
data class ExtendedColors(
    // Button states
    val primaryHover: Color,
    val destructiveHover: Color,
    val destructiveSecondaryOutline: Color,
    val disabledOutline: Color,
    val disabledFill: Color,
    val successOutline: Color,
    val success: Color,
    val onSuccess: Color,
    val secondaryFill: Color,

    // Text variants
    val textPrimary: Color,
    val textTertiary: Color,
    val textSecondary: Color,
    val textPlaceholder: Color,
    val textDisabled: Color,

    // Surface variants
    val surfaceLower: Color,
    val surfaceHigher: Color,
    val surfaceOutline: Color,
    val overlay: Color,

    // Accent colors
    val accentBlue: Color,
    val accentPurple: Color,
    val accentViolet: Color,
    val accentPink: Color,
    val accentOrange: Color,
    val accentYellow: Color,
    val accentGreen: Color,
    val accentTeal: Color,
    val accentLightBlue: Color,
    val accentGrey: Color,

    // Cake colors for chat bubbles
    val cakeViolet: Color,
    val cakeGreen: Color,
    val cakeBlue: Color,
    val cakePink: Color,
    val cakeOrange: Color,
    val cakeYellow: Color,
    val cakeTeal: Color,
    val cakePurple: Color,
    val cakeRed: Color,
    val cakeMint: Color,
)

/**
 * A predefined [ExtendedColors] instance representing the light color scheme for the application.
 *
 * This color palette provides a comprehensive set of design tokens for various UI elements, including:
 *
 * - **Button states**: Colors for different button states such as hover, destructive hover, and disabled states.
 * - **Text variants**: Colors used for text at different levels of emphasis, such as primary, secondary, tertiary, placeholder, and disabled text.
 * - **Surface variants**: Colors used for surfaces, outlines, and overlays within the UI context.
 * - **Accent colors**: A set of vibrant colors for accents used throughout the interface, including blue, purple, violet, pink, orange, yellow, green
 * , teal, light blue, and grey.
 * - **Cake colors**: Specialized colors designed for chat bubble visual elements, covering a range of hues such as violet, green, blue, pink, orange
 * , yellow, teal, purple, red, and mint.
 */
val LightExtendedColors = ExtendedColors(
    primaryHover = ChirpBrand600,
    destructiveHover = ChirpRed600,
    destructiveSecondaryOutline = ChirpRed200,
    disabledOutline = ChirpBase200,
    disabledFill = ChirpBase150,
    successOutline = ChirpBrand100,
    success = ChirpBrand600,
    onSuccess = ChirpBase0,
    secondaryFill = ChirpBase100,

    textPrimary = ChirpBase1000,
    textTertiary = ChirpBase800,
    textSecondary = ChirpBase900,
    textPlaceholder = ChirpBase700,
    textDisabled = ChirpBase400,

    surfaceLower = ChirpBase100,
    surfaceHigher = ChirpBase100,
    surfaceOutline = ChirpBase1000Alpha14,
    overlay = ChirpBase1000Alpha80,

    accentBlue = ChirpBlue,
    accentPurple = ChirpPurple,
    accentViolet = ChirpViolet,
    accentPink = ChirpPink,
    accentOrange = ChirpOrange,
    accentYellow = ChirpYellow,
    accentGreen = ChirpGreen,
    accentTeal = ChirpTeal,
    accentLightBlue = ChirpLightBlue,
    accentGrey = ChirpGrey,

    cakeViolet = ChirpCakeLightViolet,
    cakeGreen = ChirpCakeLightGreen,
    cakeBlue = ChirpCakeLightBlue,
    cakePink = ChirpCakeLightPink,
    cakeOrange = ChirpCakeLightOrange,
    cakeYellow = ChirpCakeLightYellow,
    cakeTeal = ChirpCakeLightTeal,
    cakePurple = ChirpCakeLightPurple,
    cakeRed = ChirpCakeLightRed,
    cakeMint = ChirpCakeLightMint,
)

/**
 * Represents a color palette specifically designed for dark mode themes within the application.
 * The palette contains colors for various UI elements, including button states, text styles,
 * surface variants, accent highlights, and chat bubble themes.
 *
 * This predefined set of colors enhances consistency, readability, and accessibility in dark mode contexts.
 */
val DarkExtendedColors = ExtendedColors(
    primaryHover = ChirpBrand600,
    destructiveHover = ChirpRed600,
    destructiveSecondaryOutline = ChirpRed200,
    disabledOutline = ChirpBase900,
    disabledFill = ChirpBase1000,
    successOutline = ChirpBrand500Alpha40,
    success = ChirpBrand500,
    onSuccess = ChirpBase1000,
    secondaryFill = ChirpBase900,

    textPrimary = ChirpBase0,
    textTertiary = ChirpBase200,
    textSecondary = ChirpBase150,
    textPlaceholder = ChirpBase400,
    textDisabled = ChirpBase500,

    surfaceLower = ChirpBase1000,
    surfaceHigher = ChirpBase900,
    surfaceOutline = ChirpBase100Alpha10Alt,
    overlay = ChirpBase1000Alpha80,

    accentBlue = ChirpBlue,
    accentPurple = ChirpPurple,
    accentViolet = ChirpViolet,
    accentPink = ChirpPink,
    accentOrange = ChirpOrange,
    accentYellow = ChirpYellow,
    accentGreen = ChirpGreen,
    accentTeal = ChirpTeal,
    accentLightBlue = ChirpLightBlue,
    accentGrey = ChirpGrey,

    cakeViolet = ChirpCakeDarkViolet,
    cakeGreen = ChirpCakeDarkGreen,
    cakeBlue = ChirpCakeDarkBlue,
    cakePink = ChirpCakeDarkPink,
    cakeOrange = ChirpCakeDarkOrange,
    cakeYellow = ChirpCakeDarkYellow,
    cakeTeal = ChirpCakeDarkTeal,
    cakePurple = ChirpCakeDarkPurple,
    cakeRed = ChirpCakeDarkRed,
    cakeMint = ChirpCakeDarkMint,
)

/**
 * LightColorScheme defines the color palette for the application's light theme configuration.
 *
 * This color scheme is designed to maintain proper contrast and visual hierarchy in light-themed interfaces.
 * It includes distinct color configurations for primary, secondary, tertiary, error, background, surface,
 * and other properties essential for Material Design 3, such as outline variants.
 *
 * Each color role defines the default color and its corresponding contrast color (e.g., `onPrimary` for `primary`).
 *
 * This object adheres to the Material Design guidelines for color accessibility and usability in light-themed contexts.
 */
val LightColorScheme = lightColorScheme(
    primary = ChirpBrand500,
    onPrimary = ChirpBrand1000,
    primaryContainer = ChirpBrand100,
    onPrimaryContainer = ChirpBrand900,

    secondary = ChirpBase700,
    onSecondary = ChirpBase0,
    secondaryContainer = ChirpBase100,
    onSecondaryContainer = ChirpBase900,

    tertiary = ChirpBrand900,
    onTertiary = ChirpBase0,
    tertiaryContainer = ChirpBrand100,
    onTertiaryContainer = ChirpBrand1000,

    error = ChirpRed500,
    onError = ChirpBase0,
    errorContainer = ChirpRed200,
    onErrorContainer = ChirpRed600,

    background = ChirpBrand1000,
    onBackground = ChirpBase0,
    surface = ChirpBase0,
    onSurface = ChirpBase1000,
    surfaceVariant = ChirpBase100,
    onSurfaceVariant = ChirpBase900,

    outline = ChirpBase1000Alpha8,
    outlineVariant = ChirpBase200,
)

/**
 * A dark color scheme defined for theming purposes within the application.
 *
 * The `DarkColorScheme` variable sets colors for key UI elements such as primary, secondary, and tertiary traits,
 * as well as colors for error states, background, surfaces, and outlines.
 * It ensures proper contrast and aesthetic consistency across dark-themed interfaces.
 *
 * Key attributes include:
 * - Primary and secondary colors for accent and complementary features
 * - Tertiary colors for additional emphasis
 * - Error colors for displaying critical states
 * - Background and surface colors for UI elements
 * - Outline variants to define borders and divisions
 */
val DarkColorScheme = darkColorScheme(
    primary = ChirpBrand500,
    onPrimary = ChirpBrand1000,
    primaryContainer = ChirpBrand900,
    onPrimaryContainer = ChirpBrand500,

    secondary = ChirpBase400,
    onSecondary = ChirpBase1000,
    secondaryContainer = ChirpBase900,
    onSecondaryContainer = ChirpBase150,

    tertiary = ChirpBrand500,
    onTertiary = ChirpBase1000,
    tertiaryContainer = ChirpBrand900,
    onTertiaryContainer = ChirpBrand500,

    error = ChirpRed500,
    onError = ChirpBase0,
    errorContainer = ChirpRed600,
    onErrorContainer = ChirpRed200,

    background = ChirpBase1000,
    onBackground = ChirpBase0,
    surface = ChirpBase950,
    onSurface = ChirpBase0,
    surfaceVariant = ChirpBase900,
    onSurfaceVariant = ChirpBase150,

    outline = ChirpBase100Alpha10,
    outlineVariant = ChirpBase800,
)