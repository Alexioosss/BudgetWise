package com.example.budgetwise.data.database

import android.content.Context
import androidx.room.Room
import com.example.budgetwise.data.dao.TransactionDAO
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        val db: AppDatabase = Room.databaseBuilder(context,
            AppDatabase::class.java,
            "budgetwise_db")
            .build()
        return db
    }

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDAO = db.transactionDao()
}