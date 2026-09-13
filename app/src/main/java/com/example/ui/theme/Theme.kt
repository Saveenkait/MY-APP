package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DjDarkColorScheme = darkColorScheme(
    primary = DjPrimaryCyan,
    onPrimary = Color.Black,
    primaryContainer = DjAccentPurple,
    onPrimaryContainer = Color.White,
    secondary = DjSecondaryPink,
    onSecondary = Color.White,
    secondaryContainer = DjSurfaceElevated,
    onSecondaryContainer = DjPrimaryCyan,
    tertiary = DjAccentAmber,
    onTertiary = Color.Black,
    background = DjBackground,
    onBackground = DjTextPrimary,
    surface = DjSurface,
    onSurface = DjTextPrimary,
    surfaceVariant = DjSurfaceVariant,
    onSurfaceVariant = DjTextSecondary,
    outline = DjBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Force dark club vibe for DJ Remixer app
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DjDarkColorScheme,
        typography = Typography,
        content = content
    )
}
