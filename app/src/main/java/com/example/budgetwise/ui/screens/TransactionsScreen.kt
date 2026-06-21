package com.example.budgetwise.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetwise.ui.components.PageHeader
import com.example.budgetwise.ui.components.TransactionCard
import com.example.budgetwise.ui.domain.models.Transaction
import com.example.budgetwise.ui.domain.models.TransactionTypes
import com.example.budgetwise.ui.viewmodels.TransactionsViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.abs

@Composable
fun TransactionsScreen(viewModel: TransactionsViewModel = hiltViewModel()) {
    var selectedOption by remember { mutableStateOf("All") }
    val transactions by viewModel.transactions.collectAsState()

    val filteredTransactions = remember(transactions, selectedOption) {
        transactions
            .filter {
                when(selectedOption) {
                    "Incoming" -> it.transactionType == TransactionTypes.INCOMING
                    "Outgoing" -> it.transactionType == TransactionTypes.OUTGOING
                    else -> true
                }
            }
            .sortedByDescending { it.date }
    }

    val totalIncoming = remember(transactions) {
        transactions.filter { it.transactionType == TransactionTypes.INCOMING }.sumOf { it.amount }
    }
    val totalOutgoing = remember(transactions) {
        transactions.filter { it.transactionType == TransactionTypes.OUTGOING }.sumOf { it.amount }
    }

    PageHeader(
        title = "Transactions",
        subtitle = "See how your money moves"
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(20.dp))
            TransactionsSummary(
                totalIncoming = totalIncoming,
                totalOutgoing = totalOutgoing
            )
            Spacer(modifier = Modifier.height(20.dp))
            TransactionFilter(
                selected = selectedOption,
                onSelect = { selectedOption = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if(filteredTransactions.isEmpty()) { EmptyTransactionsState() }
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    val grouped = filteredTransactions.groupBy { it.date }
                    grouped.forEach { (date, transactionsForDate) ->
                        stickyHeader(key = "header_$date") {
                            DateHeader(label = dateGroupLabel(date))
                        }
                        items(
                            items = transactionsForDate,
                            key = { it.id ?: it.hashCode().toLong() }
                        ) { transaction ->
                            TransactionRow(
                                transaction = transaction,
                                onDelete = { viewModel.deleteTransactionById(transaction.id!!) },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionsSummary(totalIncoming: Double, totalOutgoing: Double,
                                modifier: Modifier = Modifier) {
    val net: Double = totalIncoming - totalOutgoing

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surfaceVariant),
        border = BorderStroke(2.dp, colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(vertical = 18.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SummaryStat(
                label = "Income",
                amount = totalIncoming,
                icon = Icons.Default.TrendingUp,
                tint = colorScheme.primary
            )
            VerticalDivider(modifier = Modifier.fillMaxHeight())
            SummaryStat(
                label = "Expenses",
                amount = totalOutgoing,
                icon = Icons.Default.TrendingDown,
                tint = colorScheme.error
            )
            VerticalDivider(modifier = Modifier.fillMaxHeight())
            SummaryStat(
                label = "Net",
                amount = net,
                icon = Icons.Default.AccountBalanceWallet,
                tint = if(net >= 0) colorScheme.primary else colorScheme.error
            )
        }
    }
}

@Composable
private fun SummaryStat(label: String, amount: Double, icon: ImageVector,
                        tint: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = amount.asCurrency(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = tint
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun TransactionFilter(selected: String, onSelect: (String) -> Unit,
                              modifier: Modifier = Modifier) {
    val options = listOf("All", "Incoming", "Outgoing")
    SingleChoiceSegmentedButtonRow(modifier = modifier.fillMaxWidth()) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                selected = selected == option,
                onClick = { onSelect(option) },
                label = { Text(option) },
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = colorScheme.primary.copy(alpha = 0.12f),
                    activeContentColor = colorScheme.primary,
                    inactiveContainerColor = colorScheme.surfaceVariant,
                    inactiveContentColor = colorScheme.onSurfaceVariant
                )
            )
        }
    }
}

@Composable
private fun DateHeader(label: String, modifier: Modifier = Modifier) {
    Text(
        text = label,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        color = colorScheme.primary,
        modifier = modifier
            .fillMaxWidth()
            .background(colorScheme.background)
            .padding(vertical = 8.dp)
    )
}

@Composable
private fun TransactionRow(transaction: Transaction, onDelete: (Long) -> Unit,
                           modifier: Modifier = Modifier) {
    val haptic = LocalHapticFeedback.current
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if(value == SwipeToDismissBoxValue.EndToStart) {
                transaction.id?.let { id ->
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onDelete(id)
                }
                true
            } else { false }
        },
        positionalThreshold = { distance -> distance * 0.8f }
    )

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp)
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                DeleteBackground()
            }
        },
        content = {
            TransactionCard(
                id = transaction.id ?: 0L,
                dateTime = transaction.date.format(
                    DateTimeFormatter.ofPattern("d MMMM yyyy")),
                amount = transaction.amount,
                category = transaction.category,
                notes = transaction.notes,
                transactionType = transaction.transactionType,
                recurrenceInterval = null
            )
        }
    )
}

@Composable
private fun DeleteBackground(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(colorScheme.errorContainer)
            .padding(end = 16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Delete",
                color = colorScheme.onErrorContainer,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete transaction",
                tint = colorScheme.onErrorContainer,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

@Composable
private fun EmptyTransactionsState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 64.dp, start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "No transactions yet",
            style = MaterialTheme.typography.titleLarge,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Transactions you add will show up here",
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

private fun dateGroupLabel(date: LocalDateTime): String {
    val newDate = date.toLocalDate()
    val today = LocalDate.now()
    return when(newDate) {
        today -> "Today"
        today.minusDays(1) -> "Yesterday"
        else -> date.format(DateTimeFormatter.ofPattern("EEEE, d MMMM"))
    }
}

private fun Double.asCurrency(): String {
    val formatted: String = "%,.2f".format(abs(this))
    return if(this < 0) "-£$formatted" else "£$formatted"
}