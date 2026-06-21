package com.example.budgetwise.data.repositories

import com.example.budgetwise.ui.domain.models.Transaction

interface TransactionRepository {
    suspend fun insert(transaction: Transaction)
    suspend fun insertAll(transactions: List<Transaction>)
    suspend fun getById(id: Long): Transaction?
    suspend fun getByDateRange(start: Long, end: Long): List<Transaction>
    suspend fun getUpcoming(now: Long): List<Transaction>
    suspend fun getAll(): List<Transaction>
    suspend fun deleteById(id: Long)
    suspend fun deleteAll()
}