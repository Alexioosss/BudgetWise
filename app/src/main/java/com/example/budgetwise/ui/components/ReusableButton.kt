package com.example.budgetwise.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ReusableButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    containerColour: Color = colorScheme.primary,
    contentColour: Color,
    textColour: Color?,
    onClick: (() -> Unit)? = null
) {
    Button(
        onClick = { if(onClick != null) { onClick() } },
        enabled = enabled,
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(60.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColour,
            contentColor = contentColour,
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = textColour ?: if(enabled) { colorScheme.surface }
                else { colorScheme.surface.copy(alpha = 0.9f) }
        )
    }
}