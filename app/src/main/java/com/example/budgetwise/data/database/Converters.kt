package com.example.budgetwise.data.database

import androidx.room.TypeConverter
import com.example.budgetwise.ui.domain.models.TransactionRecurrence
import com.example.budgetwise.ui.domain.models.TransactionTypes

class Converters {
    @TypeConverter
    fun fromTransactionType(value: TransactionTypes): String = value.name

    @TypeConverter
    fun toTransactionType(value: String): TransactionTypes = TransactionTypes.valueOf(value)

    @TypeConverter
    fun fromTransactionRecurrence(value: TransactionRecurrence?): String? = value?.name

    @TypeConverter
    fun toTransactionRecurrence(value: String?): TransactionRecurrence? =
        value?.let { TransactionRecurrence.valueOf(it) }
}