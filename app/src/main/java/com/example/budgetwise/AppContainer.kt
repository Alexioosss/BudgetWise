package com.example.budgetwise

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.budgetwise.data.dao.TransactionDAO
import com.example.budgetwise.data.database.AppDatabase
import com.example.budgetwise.data.mappers.TransactionMapper
import com.example.budgetwise.data.repositories.TransactionRepository
import com.example.budgetwise.ui.domain.model.Transaction

object AppContainer {
    private lateinit var database: AppDatabase
    private lateinit var transactionDao: TransactionDAO
    lateinit var transactionRepository: TransactionRepository

    fun init(context: Context) {
        database = AppDatabase.getDatabase(context)
        transactionDao = database.transactionDao()
        transactionRepository = TransactionRepository(
            dao = transactionDao,
            entityToDomain = TransactionMapper.entityToDomain,
            domainToEntity = TransactionMapper.domainToEntity
        )
    }

    fun terminate() {
        if(::database.isInitialized) {
            database.close()
        }
    }
}