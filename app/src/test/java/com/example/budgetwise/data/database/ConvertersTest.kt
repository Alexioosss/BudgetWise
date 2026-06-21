package com.example.budgetwise.data.database

import com.example.budgetwise.ui.domain.models.TransactionTypes
import org.junit.Test

class ConvertersTest {
    val converter = Converters()
    @Test
    fun fromTransactionType_WhenTransactionType_ReturnsTransactionTypeString() {
        // Arrange
        val type: TransactionTypes = TransactionTypes.INCOMING
        // Act
        val convertedValue: String = converter.fromTransactionType(type)
        // Assert
        assert(type.name == convertedValue)
    }

    @Test
    fun toTransactionType_WhenString_ReturnsTransactionType() {
        // Arrange
        val string = "INCOMING"
        // Act
        val transactionType: TransactionTypes = converter.toTransactionType(string)
        // Assert
        assert(transactionType.name == string)
    }
}