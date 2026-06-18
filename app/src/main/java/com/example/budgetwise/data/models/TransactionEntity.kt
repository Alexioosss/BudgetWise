package com.example.budgetwise.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.budgetwise.ui.domain.models.TransactionTypes

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val date: Long,
    val amount: Double,
    val category: String,
    val notes: String?,
    val transactionType: TransactionTypes,
    val recurringDate: Long?
)