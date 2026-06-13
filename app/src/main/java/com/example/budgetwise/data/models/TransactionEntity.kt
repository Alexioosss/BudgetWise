package com.example.budgetwise.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: Int,
    val date: Long,
    val amount: Double,
    val category: String,
    val notes: String?
)