package com.example.budgetwise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.budgetwise.ui.components.PageHeader
import com.example.budgetwise.ui.domain.models.FontSize
import com.example.budgetwise.ui.domain.models.ThemeMode

@Composable
fun SettingsScreen(
    selectedTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit,
    currentFontSize: FontSize,
    onFontSizeChange: (FontSize) -> Unit
) {
    PageHeader(
        title = "Settings",
        subtitle = "Adjust app settings to suit your preferences"
    ) {
        Spacer(
            modifier = Modifier.height(25.dp)
        )
        Text(
            text = "Theme",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        ThemeMode.entries.forEach { theme ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onThemeSelected(theme) }
                    .padding(vertical = 2.dp)
                    .background(colorScheme.background),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = selectedTheme == theme,
                    onClick = { onThemeSelected(theme) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorScheme.primary,
                        unselectedColor = colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = theme.name
                        .lowercase()
                        .replaceFirstChar { it.uppercase() }
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Font Size",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        FontSize.entries.forEach { size ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onFontSizeChange(size) }
                    .padding(vertical = 2.dp)
                    .background(colorScheme.background),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = currentFontSize == size,
                    onClick = { onFontSizeChange(size) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = colorScheme.primary,
                        unselectedColor = colorScheme.onSurface
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = size.name
                    .lowercase()
                    .replaceFirstChar { it.uppercase() })
            }
        }
    }
}