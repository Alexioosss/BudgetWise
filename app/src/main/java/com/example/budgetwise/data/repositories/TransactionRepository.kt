package com.example.budgetwise.data.repositories

import com.example.budgetwise.data.dao.TransactionDAO
import com.example.budgetwise.data.mappers.toDomain
import com.example.budgetwise.data.mappers.toEntity
import com.example.budgetwise.ui.domain.models.Transaction
import javax.inject.Inject


class TransactionRepository @Inject constructor(
    private val dao: TransactionDAO
) {
    suspend fun getAllTransactions(): List<Transaction> =
        dao.getAllTransactions().map { it.toDomain() }

    suspend fun getTransaction(id: Long): Transaction? =
        dao.getTransactionById(id)?.toDomain()

    suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(transaction.toEntity())
    }

    suspend fun deleteTransactionById(id: Long) {
        dao.deleteById(id)
    }
}