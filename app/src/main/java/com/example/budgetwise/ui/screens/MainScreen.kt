package com.example.budgetwise.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetwise.ui.components.DailyOverviewCard
import com.example.budgetwise.ui.components.PageHeader
import com.example.budgetwise.ui.components.TransactionCard
import com.example.budgetwise.ui.viewmodels.TransactionsViewModel
import java.time.format.DateTimeFormatter

@Composable
fun MainScreen(viewModel: TransactionsViewModel = hiltViewModel()) {
    val tomorrowTransactions by viewModel.tomorrowTransactions.collectAsState()
    val dailyOverview by viewModel.todayOverview.collectAsState()

    PageHeader(
        title = "Budget Wise",
        subtitle = null
    ) {
        Spacer(modifier = Modifier.height(100.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(0.9f),
                contentAlignment = Alignment.Center
            ) {
                DailyOverviewCard(dailyOverview)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        if(tomorrowTransactions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(
                    tomorrowTransactions,
                    key = { it.id ?: it.hashCode().toLong() }) { tomorrowTransaction ->
                    TransactionCard(
                        id = tomorrowTransaction.id ?: 0L,
                        dateTime = tomorrowTransaction.date.format(
                            DateTimeFormatter.ofPattern("d MMMM yyyy")
                        ),
                        amount = tomorrowTransaction.amount,
                        category = tomorrowTransaction.category,
                        notes = tomorrowTransaction.notes,
                        transactionType = tomorrowTransaction.transactionType,
                        recurrenceInterval = tomorrowTransaction.recurrence
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
                        text = "No upcoming transactions for tomorrow",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun MainScreenPreview() {
    MainScreen()
}