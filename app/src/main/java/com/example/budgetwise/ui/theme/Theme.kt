package com.example.budgetwise.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.budgetwise.ui.domain.model.FontSize

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF2E7D6B),
    onPrimary = LightText,
    background = SoftMint,
    onBackground = LightText,
    surface = Color.White,
    onSurface = LightText
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4DB6AC),
    onPrimary = DarkText,
    background = DarkTeal,
    onBackground = DarkText,
    surface = Color(0xFF121212),
    onSurface = DarkText
)

@Composable
fun BudgetWiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontSize: FontSize = FontSize.MEDIUM,
    content: @Composable () -> Unit
) {
    val scale = when (fontSize) {
        FontSize.SMALL -> 0.85f
        FontSize.MEDIUM -> 1f
        FontSize.LARGE -> 1.2f
    }
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = LocalDensity.current.density,
            fontScale = LocalDensity.current.fontScale * scale
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}