package com.example.budgetwise.data.repositories

import com.example.budgetwise.data.dao.TransactionDAO
import com.example.budgetwise.data.mappers.toDomain
import com.example.budgetwise.data.mappers.toEntity
import com.example.budgetwise.ui.domain.models.Transaction
import javax.inject.Inject

class RoomTransactionRepository @Inject constructor(
    private val dao: TransactionDAO): TransactionRepository {

    override suspend fun insert(transaction: Transaction) {
        dao.insertTransaction(transaction.toEntity())
    }

    override suspend fun insertAll(transactions: List<Transaction>) {
        dao.insertAll(transactions.map { it.toEntity() })
    }

    override suspend fun getById(id: Long): Transaction? {
        return dao.getTransactionById(id)?.toDomain()
    }

    override suspend fun getByDateRange(start: Long, end: Long): List<Transaction> {
        return dao.getTransactionsInDateRange(start, end).map { it.toDomain()}
    }

    override suspend fun getUpcoming(now: Long): List<Transaction> {
        return dao.getUpcomingTransactions(now).map { it.toDomain() }
    }

    override suspend fun getAll(): List<Transaction> {
        return dao.getAllTransactions().map { it.toDomain() }
    }

    override suspend fun deleteById(id: Long) { dao.deleteById(id) }

    override suspend fun deleteAll() { dao.deleteAll() }
}