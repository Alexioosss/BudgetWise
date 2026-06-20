package com.example.budgetwise.data.repositories

import com.example.budgetwise.data.dao.TransactionDAO
import com.example.budgetwise.data.mappers.toDomain
import com.example.budgetwise.data.mappers.toEntity
import com.example.budgetwise.ui.domain.models.Transaction
import javax.inject.Inject

class TransactionRepositoryImplementation @Inject constructor(
    private val dao: TransactionDAO): TransactionRepository {
    override suspend fun getAll(): List<Transaction> {
        return dao.getAllTransactions().map { it.toDomain() }
    }

    override suspend fun deleteAll() { dao.deleteAll() }

    override suspend fun getById(id: Long): Transaction? {
        return dao.getTransactionById(id)?.toDomain()
    }

    override suspend fun insert(transaction: Transaction) {
        dao.insertTransaction(transaction.toEntity())
    }

    override suspend fun insertAll(transactionEntities: List<Transaction>) {
        dao.insertAll(transactionEntities.map { it.toEntity() })
    }

    override suspend fun deleteById(id: Long) { dao.deleteById(id) }
}