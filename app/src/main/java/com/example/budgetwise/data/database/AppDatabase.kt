package com.example.budgetwise.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.budgetwise.data.dao.TransactionDAO
import com.example.budgetwise.data.models.TransactionEntity

@Database(entities = [TransactionEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDAO
}