package com.example.budgetwise.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.budgetwise.ui.components.TransactionCard

@Composable
fun TransactionsScreen() {
    var selectedOption by remember { mutableStateOf("Incoming") }
    val borderColour = MaterialTheme.colorScheme.primary
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Transactions",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Manage your transactions here",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
        )
        Spacer(
            modifier = Modifier.height(25.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            listOf("Incoming", "Outgoing").forEachIndexed { index, option ->
                val isSelected = selectedOption == option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedOption = option }
                        .then(
                            if(isSelected) {
                                Modifier.drawBehind {
                                    val stroke = 4.dp.toPx()
                                    val half = stroke / 2
                                    drawLine(borderColour,
                                        Offset(0f, half),
                                        Offset(size.width, half), stroke)
                                    val sideX = if(index == 0) size.width else 0f
                                    drawLine(borderColour,
                                        Offset(sideX, half),
                                        Offset(sideX, size.height), stroke / 2)
                                }
                            } else {
                                Modifier.drawBehind {
                                    val stroke = 2.dp.toPx()
                                    drawLine(borderColour,
                                        Offset(0f, size.height),
                                        Offset(size.width, size.height), stroke)
                                }
                            }
                        )
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        style = MaterialTheme.typography.headlineSmall,
                        color = borderColour,
                        fontWeight = if(isSelected) FontWeight.Bold
                                     else FontWeight.Normal
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val stroke = 2.dp.toPx()
                    val half = stroke / 2
                    drawLine(borderColour,
                        Offset(half, 0f),
                        Offset(half, size.height), stroke)
                    drawLine(borderColour,
                        Offset(size.width - half, 0f),
                        Offset(size.width - half, size.height), stroke)
                    drawLine(borderColour,
                            Offset(0f, size.height - half),
                        Offset(size.width, size.height - half), stroke)
                }
                .padding(16.dp)
        ) {
            if(selectedOption == "Incoming") {
                // Display incoming transactions here - Use TransactionsCard UI component
            } else if(selectedOption == "Outgoing") {
                // Display outgoing transactions here - Use TransactionsCard UI component
            }
        }
    }
}