package com.example.hw_03.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color






private val LightColorScheme = lightColorScheme(
    primary = RedPrimary,
    onPrimary = BackgroundLight,
    primaryContainer = RedPrimaryLight,
    onPrimaryContainer = TextPrimary,
    secondary = RedAccent,
    onSecondary = BackgroundLight,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceLight,
    onSurface = TextPrimary,
    error = ErrorColor,
    onError = BackgroundLight
)


private val DarkColorScheme = darkColorScheme(
    primary = RedPrimary,
    onPrimary = BackgroundLight,
    primaryContainer = RedPrimaryDark,
    onPrimaryContainer = BackgroundLight,
    secondary = RedAccent,
    onSecondary = BackgroundLight,
    background = Color(0xFF121212),
    onBackground = BackgroundLight,
    surface = Color(0xFF1E1E1E),
    onSurface = BackgroundLight,
    error = ErrorColor,
    onError = BackgroundLight
)


@Composable
fun HW3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
