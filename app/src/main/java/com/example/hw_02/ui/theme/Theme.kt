package com.example.hw_02.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


enum class ThemeType {
    DEFAULT, RED, GREEN, BLUE
}

@Composable
fun NotesAppTheme(
    themeType: ThemeType = ThemeType.DEFAULT,
    content: @Composable () -> Unit
) {
    val colorScheme = when (themeType) {
        ThemeType.DEFAULT -> lightColorScheme(
            primary = DefaultColors.Primary,
            secondary = DefaultColors.Secondary,
            background = DefaultColors.Background,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black
        )
        ThemeType.RED -> lightColorScheme(
            primary = RedColors.Primary,
            secondary = RedColors.Secondary,
            background = RedColors.Background,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black
        )
        ThemeType.GREEN -> lightColorScheme(
            primary = GreenColors.Primary,
            secondary = GreenColors.Secondary,
            background = GreenColors.Background,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black
        )
        ThemeType.BLUE -> lightColorScheme(
            primary = BlueColors.Primary,
            secondary = BlueColors.Secondary,
            background = BlueColors.Background,
            onPrimary = Color.White,
            onSecondary = Color.Black,
            onBackground = Color.Black
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


