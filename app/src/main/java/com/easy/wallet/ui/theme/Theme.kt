package com.easy.wallet.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.easy.core.ui.Dimensions
import com.easy.core.ui.LocalSpacing

private val DarkColorPalette = EasyComposeColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200,
    secondaryVariant = Teal200,
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFCF6679),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black,
    purple = Purple200,
    isLight = false
)

private val LightColorPalette = EasyComposeColors(
    primary = Color(0xFFBB86FC),
    primaryVariant = Color(0xFF3700B3),
    secondary = Color(0xFF03DAC6),
    secondaryVariant = Color(0xFF03DAC6),
    background = Color(0xFF121212),
    surface = Color(0xFF121212),
    error = Color(0xFFCF6679),
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    onError = Color.Black,
    purple = Purple200,
    isLight = true
)

private val LocalEasyColors = compositionLocalOf {
    LightColorPalette
}

object EasyTheme {
    val colors: EasyComposeColors
        @Composable
        get() = LocalEasyColors.current
}

@Composable
fun EasyTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val targetColors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    val colors = EasyComposeColors(
        primary = targetColors.primary,
        primaryVariant = targetColors.primaryVariant,
        secondary = targetColors.secondary,
        secondaryVariant = targetColors.secondaryVariant,
        background = targetColors.background,
        surface = targetColors.surface,
        error = targetColors.error,
        onPrimary = targetColors.onPrimary,
        onSecondary = targetColors.onSecondary,
        onBackground = targetColors.onBackground,
        onSurface = targetColors.onSurface,
        onError = targetColors.onError,
        purple = targetColors.purple,
        isLight = targetColors.isLight
    )

    CompositionLocalProvider(
        LocalSpacing provides Dimensions(),
        LocalEasyColors provides colors
    ) {
        MaterialTheme(
            typography = Typography,
            shapes = Shapes,
            content = content
        )
    }
}