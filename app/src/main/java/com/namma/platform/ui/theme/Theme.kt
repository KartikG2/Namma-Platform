package com.namma.platform.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Fixed light-only color scheme — no dynamic colors.
 *
 * Rationale: we lock to a curated WCAG-AA palette so every
 * text / background combination stays accessible regardless
 * of device OEM theming.
 */
private val NammaLightColorScheme = lightColorScheme(
    // Primary
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = PrimaryBlueDark,

    // Secondary (Yellow platform accent)
    secondary = PlatformYellow,
    onSecondary = TextPrimary,
    secondaryContainer = YellowLight,
    onSecondaryContainer = TextPrimary,

    // Tertiary (Success green)
    tertiary = SuccessGreen,
    onTertiary = Color.White,
    tertiaryContainer = SuccessGreen.copy(alpha = 0.12f),
    onTertiaryContainer = SuccessGreen,

    // Error
    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed.copy(alpha = 0.12f),
    onErrorContainer = ErrorRed,

    // Background & Surface
    background = BackgroundColor,
    onBackground = TextPrimary,
    surface = SurfaceColor,
    onSurface = TextPrimary,
    surfaceVariant = Color(0xFFF5F5F5),
    onSurfaceVariant = TextSecondary,

    // Outline / Divider
    outline = Divider,
    outlineVariant = Divider.copy(alpha = 0.5f)
)

/**
 * NammaPlatformTheme
 *
 * Light-only Material 3 theme with a fixed WCAG-AA
 * accessible palette. Dynamic color is intentionally
 * disabled to guarantee consistent branding.
 */
@Composable
fun NammaPlatformTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = NammaLightColorScheme

    // Tint the system status bar to match our background
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view)
                .isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NammaTypography,
        content = content
    )
}
