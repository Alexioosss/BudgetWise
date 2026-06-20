package com.example.budgetwise.data.repositories

import com.example.budgetwise.ui.domain.models.Transaction

interface TransactionRepository {
    suspend fun insert(transaction: Transaction)
    suspend fun insertAll(transactionEntities: List<Transaction>)
    suspend fun getById(id: Long): Transaction?
    suspend fun getAll(): List<Transaction>
    suspend fun deleteAll()
    suspend fun deleteById(id: Long)
}