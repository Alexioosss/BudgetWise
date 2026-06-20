package com.example.budgetwise.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import com.example.budgetwise.ui.domain.models.FontSize

private val LightColorScheme = lightColorScheme(
    background = SoftMint,
    surface = LightSurface,
    surfaceVariant = LightSurfaceVariant,
    onBackground = LightText,
    onSurface = LightText,
    onSurfaceVariant = LightTextSecondary,
    primary = IncomingGreen,
    error = OutgoingRed,
    outlineVariant = NeutralIcon
)

private val DarkColorScheme = darkColorScheme(
    background = DarkSurface,
    surface = DarkSurface,
    surfaceVariant = DarkSurfaceVariant,
    onBackground = DarkText,
    onSurface = DarkText,
    onSurfaceVariant = DarkTextSecondary,
    primary = IncomingGreenDark,
    error = OutgoingRedDark,
    outlineVariant = NeutralIconDark
)

@Composable
fun BudgetWiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    fontSize: FontSize = FontSize.MEDIUM,
    content: @Composable () -> Unit
) {
    val colorScheme = if(darkTheme) DarkColorScheme else LightColorScheme

    CompositionLocalProvider(
        LocalDensity provides Density(
            density = LocalDensity.current.density,
            fontScale = LocalDensity.current.fontScale * fontSize.scaleFactor
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}