package com.example.budgetwise.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.budgetwise.data.models.TransactionEntity

@Dao
interface TransactionDAO {
    @Insert suspend fun insertTransaction(transactionEntity: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(transactionEntities: List<TransactionEntity>)

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransactionById(id: Long): TransactionEntity?

    @Query("SELECT * FROM transactions WHERE date BETWEEN :start AND :end")
    suspend fun getTransactionsInDateRange(start: Long, end: Long): List<TransactionEntity>

    @Query("SELECT * FROM transactions WHERE date > :now ORDER BY date ASC")
    suspend fun getUpcomingTransactions(now: Long): List<TransactionEntity>

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionEntity>

    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()
}