package com.example.budgetwise.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgetwise.data.dao.TransactionDAO
import com.example.budgetwise.data.models.TransactionEntity
import com.example.budgetwise.ui.domain.models.Categories
import com.example.budgetwise.ui.domain.models.TransactionTypes
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        lateinit var db: AppDatabase
        db = Room.databaseBuilder(context,
            AppDatabase::class.java,
            "budgetwise_db")
            .addCallback(object : RoomDatabase.Callback() {
                override fun onOpen(supportDb: SupportSQLiteDatabase) {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dao = db.transactionDao()
                        dao.deleteAll()
                        populateDatabase(dao)
                    }
                }
            })
            .build()
        return db
    }

    @Provides
    fun provideTransactionDao(db: AppDatabase): TransactionDAO = db.transactionDao()

    suspend fun populateDatabase(transactionDAO: TransactionDAO) {
        val currentTime = System.currentTimeMillis()
        val oneMonthLater = Calendar.getInstance().apply {
            timeInMillis = currentTime
            add(Calendar.MONTH, 1)
        }.timeInMillis
        transactionDAO.insertAll(listOf(
            TransactionEntity(
                id = 1,
                date = currentTime - 86400000,
                amount = 40.00,
                category = Categories.SUBSCRIPTIONS.name,
                notes = "Monthly gym subscription",
                transactionType = TransactionTypes.OUTGOING,
                recurringDate = oneMonthLater
            ),
            TransactionEntity(
                id = 2,
                date = System.currentTimeMillis() - 864000000,
                amount = 150.00,
                category = Categories.SALARY.name,
                notes = "Weekly work payment",
                transactionType = TransactionTypes.INCOMING,
                recurringDate = null
            )
        ))
    }
}