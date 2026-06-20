package com.example.budgetwise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetwise.ui.components.PageHeader
import com.example.budgetwise.ui.components.TransactionCard
import com.example.budgetwise.ui.viewmodels.TransactionsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun UpcomingScreen(
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val upcomingTransactions by viewModel.upcomingTransactions.collectAsState()

    PageHeader(
        title = "Upcoming Transactions",
        subtitle = "View your upcoming transactions here"
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        if(upcomingTransactions.isEmpty()) {
            LazyColumn(modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
            ) {
                items(
                    upcomingTransactions,
                    key = { it.id ?: it.hashCode().toLong() }) { transaction ->
                    TransactionCard(
                        id = transaction.id ?: 0L,
                        dateTime = transaction.date.format(
                            DateTimeFormatter.ofPattern("d MMMM yyyy")
                        ),
                        amount = transaction.amount,
                        category = transaction.category,
                        notes = transaction.notes,
                        transactionType = transaction.transactionType,
                        recurrenceInterval = transaction.recurrence
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorScheme.surfaceVariant)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No upcoming transactions",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}