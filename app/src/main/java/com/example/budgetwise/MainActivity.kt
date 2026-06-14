package com.example.budgetwise

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.budgetwise.ui.domain.model.FontSize
import com.example.budgetwise.ui.domain.model.ThemeMode
import com.example.budgetwise.ui.navigation.AppNavigation
import com.example.budgetwise.ui.theme.BudgetWiseTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            var themeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }
            var fontSize by remember { mutableStateOf(FontSize.MEDIUM) }
            BudgetWiseTheme(
                darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> isSystemInDarkTheme()
                },
                fontSize = fontSize
            ) {
                val navController = rememberNavController()
                AppNavigation(
                    navController,
                    currentThemeMode = themeMode,
                    onThemeModeChange = { newTheme -> themeMode = newTheme },
                    currentFontSize = fontSize,
                    onFontSizeChange = { fontSize = it }
                )
            }
        }
    }
}