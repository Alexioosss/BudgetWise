package com.example.budgetwise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetwise.ui.components.PageHeader
import com.example.budgetwise.ui.components.TransactionCard
import com.example.budgetwise.ui.domain.models.TransactionTypes
import com.example.budgetwise.ui.viewmodels.TransactionsViewModel
import kotlinx.coroutines.delay
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    var selectedOption by remember { mutableStateOf("Incoming") }
    val borderColour = colorScheme.primary
    val transactions by viewModel.transactions.collectAsState()
    val filteredTransactions = transactions.filter {
        when(selectedOption) {
            "Incoming" -> it.transactionType == TransactionTypes.INCOMING
            "Outgoing" -> it.transactionType == TransactionTypes.OUTGOING
            else -> true
        }
    }
    val haptic = LocalHapticFeedback.current
    PageHeader(
        title = "Transactions",
        subtitle = "Manage your transactions here"
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Incoming", "Outgoing").forEachIndexed { _, option ->
                val isSelected = selectedOption == option
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { selectedOption = option }
                        .then(
                            if(isSelected) {
                                Modifier.drawBehind {
                                    val stroke = 3.dp.toPx()
                                    drawLine(
                                        borderColour, Offset(0f, 0f),
                                        Offset(size.width, 0f), stroke
                                    )
                                    drawLine(
                                        borderColour, Offset(0f, 0f),
                                        Offset(0f, size.height), stroke
                                    )
                                    drawLine(
                                        borderColour, Offset(size.width, 0f),
                                        Offset(size.width, size.height), stroke
                                    )
                                }
                            } else {
                                Modifier.drawBehind {
                                    val stroke = 1.dp.toPx()
                                    drawLine(
                                        borderColour, Offset(0f, size.height),
                                        Offset(size.width, size.height), stroke
                                    )
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
                        fontWeight = if (isSelected) FontWeight.Bold
                        else FontWeight.Normal
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val stroke = 2.dp.toPx()
                    val half = stroke / 2
                    drawLine(
                        borderColour,
                        Offset(half, 0f),
                        Offset(half, size.height), stroke
                    )
                    drawLine(
                        borderColour,
                        Offset(size.width - half, 0f),
                        Offset(size.width - half, size.height), stroke
                    )
                    drawLine(
                        borderColour,
                        Offset(0f, size.height - half),
                        Offset(size.width, size.height - half), stroke
                    )
                }
                .padding(16.dp)
        ) {
            items(filteredTransactions, key = { it.id ?: it.hashCode().toLong() }) { transaction ->
                val dismissState = rememberSwipeToDismissBoxState(
                    positionalThreshold = { totalDistance -> totalDistance * 0.85f }
                )
                var deleted by remember { mutableStateOf(false) }
                LaunchedEffect(dismissState.targetValue) {
                    if(dismissState.targetValue == SwipeToDismissBoxValue.EndToStart && !deleted) {
                        deleted = true
                        delay(150.milliseconds)
                        haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        transaction.id?.let { id ->
                            viewModel.deleteTransactionById(id)
                        }
                    }
                }
                if(!deleted) {
                    SwipeToDismissBox(
                        state = dismissState,
                        enableDismissFromStartToEnd = false,
                        enableDismissFromEndToStart = true,
                        content = {
                            TransactionCard(
                                id = transaction.id ?: 0L,
                                dateTime = transaction.date.format(
                                    DateTimeFormatter.ofPattern("d MMMM yyyy")
                                ),
                                amount = transaction.amount,
                                category = transaction.category,
                                notes = transaction.notes,
                                transactionType = transaction.transactionType,
                                recurrenceInterval = null
                            )
                        },
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 6.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(colorScheme.error.copy(alpha = 0.2f))
                                    .padding(38.dp),
                                contentAlignment = Alignment.CenterEnd
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = colorScheme.error
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}