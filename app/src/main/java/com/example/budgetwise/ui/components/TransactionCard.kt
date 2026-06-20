package com.example.budgetwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.budgetwise.ui.domain.models.Categories
import com.example.budgetwise.ui.domain.models.TransactionRecurrence
import com.example.budgetwise.ui.domain.models.TransactionTypes

@Composable
fun TransactionCard(
    modifier: Modifier = Modifier,
    id: Long,
    dateTime: String,
    amount: Double,
    category: Categories,
    notes: String? = null,
    transactionType: TransactionTypes,
    recurrenceInterval: TransactionRecurrence?
) {
    val amountColor: Color = if(transactionType == TransactionTypes.INCOMING)
        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
    val amountPrefix: String = if(transactionType == TransactionTypes.INCOMING) "+ £" else "- £"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = category.label,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = if(recurrenceInterval != null) "$dateTime - recurring " +
                        recurrenceInterval.name.lowercase() else dateTime,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            if(!notes.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Divider(
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .height(1.3.dp)
                        .fillMaxWidth(0.85f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = notes,
                    style = MaterialTheme.typography.bodySmall,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Text(
            text = "$amountPrefix${"%.2f".format(amount)}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = amountColor
        )
    }
}