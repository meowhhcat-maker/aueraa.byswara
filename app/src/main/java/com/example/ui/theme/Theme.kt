package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val AueraaDarkColorScheme = darkColorScheme(
    primary = AueraaElectricBlue,
    onPrimary = AueraaTextPrimary,
    primaryContainer = AueraaSurfaceVariant,
    onPrimaryContainer = AueraaNeonCyan,
    secondary = AueraaRoyalPurple,
    onSecondary = AueraaTextPrimary,
    secondaryContainer = AueraaSurfaceVariant,
    onSecondaryContainer = AueraaRoyalPurple,
    tertiary = AueraaNeonCyan,
    onTertiary = AueraaDarkBackground,
    background = AueraaDarkBackground,
    onBackground = AueraaTextPrimary,
    surface = AueraaSurfaceDark,
    onSurface = AueraaTextPrimary,
    surfaceVariant = AueraaSurfaceVariant,
    onSurfaceVariant = AueraaTextSecondary,
    outline = AueraaSurfaceBorder,
    error = AueraaErrorRose
)

private val AueraaLightColorScheme = lightColorScheme(
    primary = AueraaElectricBlue,
    onPrimary = AueraaTextPrimary,
    primaryContainer = Color(0xFFE0EDFF),
    onPrimaryContainer = AueraaElectricBlue,
    secondary = AueraaRoyalPurple,
    onSecondary = AueraaTextPrimary,
    secondaryContainer = Color(0xFFF3E8FF),
    onSecondaryContainer = AueraaRoyalPurple,
    tertiary = AueraaNeonCyan,
    onTertiary = AueraaDarkBackground,
    background = AueraaLightBackground,
    onBackground = AueraaLightTextPrimary,
    surface = AueraaLightSurface,
    onSurface = AueraaLightTextPrimary,
    surfaceVariant = AueraaLightSurfaceVariant,
    onSurfaceVariant = AueraaLightTextSecondary,
    outline = Color(0xFFCBD5E1),
    error = AueraaErrorRose
)

@Composable
fun AueraaDigitalTheme(
    darkTheme: Boolean = true, // Default to brand luxury dark mode
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) AueraaDarkColorScheme else AueraaLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = colorScheme.background.toArgb()
                window.navigationBarColor = colorScheme.background.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Retain alias for compatibility
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    AueraaDigitalTheme(darkTheme = darkTheme, content = content)
}
