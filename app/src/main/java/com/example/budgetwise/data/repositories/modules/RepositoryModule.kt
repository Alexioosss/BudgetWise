package com.example.budgetwise.data.repositories.modules

import com.example.budgetwise.data.dao.TransactionDAO
import com.example.budgetwise.data.mappers.toDomain
import com.example.budgetwise.data.mappers.toEntity
import com.example.budgetwise.data.models.TransactionEntity
import com.example.budgetwise.data.repositories.TransactionRepository
import com.example.budgetwise.ui.domain.models.Transaction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    fun provideEntityToDomain(): (TransactionEntity) -> Transaction = { entity ->
        entity.toDomain()
    }

    @Provides
    fun provideDomainToEntity(): (Transaction) -> TransactionEntity = { domain ->
        domain.toEntity()
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        dao: TransactionDAO
    ): TransactionRepository {
        return TransactionRepository(dao)
    }
}