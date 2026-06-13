package com.example.budgetwise.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.budgetwise.data.models.TransactionEntity
import com.example.budgetwise.ui.domain.model.Transaction

@Dao
interface TransactionDAO {
    @Insert suspend fun insertTransaction(transactionEntity: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Int): TransactionEntity?

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionEntity>
}