package com.example.budgetwise.data.repositories.modules

import com.example.budgetwise.data.dao.TransactionDAO
import com.example.budgetwise.data.repositories.TransactionRepository
import com.example.budgetwise.data.repositories.TransactionRepositoryImplementation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides @Singleton
    fun provideTransactionRepository(
        dao: TransactionDAO
    ): TransactionRepository {
        return TransactionRepositoryImplementation(dao)
    }
}