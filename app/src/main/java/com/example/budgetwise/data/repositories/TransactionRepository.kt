package com.example.budgetwise.data.repositories

import com.example.budgetwise.data.dao.TransactionDAO
import com.example.budgetwise.data.models.TransactionEntity
import com.example.budgetwise.ui.domain.model.Transaction


class TransactionRepository(
    private val dao: TransactionDAO,
    private val entityToDomain: (TransactionEntity) -> Transaction,
    private val domainToEntity: (Transaction) -> TransactionEntity
) {
    suspend fun getAllTransactions(): List<Transaction> {
        val entities = dao.getAllTransactions()
        return entities.stream().map{ entity -> this.entityToDomain(entity) }.toList()
    }

    suspend fun getTransaction(id: Int): Transaction? {
        val entity = dao.getTransactionById(id)
        return entity?.let { this.entityToDomain(it) }
    }

    suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransaction(this.domainToEntity(transaction))
    }
}