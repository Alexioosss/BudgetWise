package com.example.budgetwise.data.mappers.modules

import com.example.budgetwise.data.mappers.toDomain
import com.example.budgetwise.data.mappers.toEntity
import com.example.budgetwise.data.models.TransactionEntity
import com.example.budgetwise.ui.domain.models.Transaction
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module @InstallIn(SingletonComponent::class)
object MapperModule {
    @Provides
    fun provideDomainToEntity(): (Transaction) -> TransactionEntity {
        return { transaction -> transaction.toEntity() }
    }

    @Provides
    fun provideEntityToDomain(): (TransactionEntity) -> Transaction {
        return { transactionEntity -> transactionEntity.toDomain() }
    }
}