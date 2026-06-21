package com.example.budgetwise.data.repositories.modules

import com.example.budgetwise.data.repositories.RoomTransactionRepository
import com.example.budgetwise.data.repositories.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module @InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindTransactionRepository(impl: RoomTransactionRepository): TransactionRepository
}