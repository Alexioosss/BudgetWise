package com.example.budgetwise.data.mappers

import com.example.budgetwise.data.models.TransactionEntity
import com.example.budgetwise.ui.domain.models.Categories
import com.example.budgetwise.ui.domain.models.Transaction
import com.example.budgetwise.ui.domain.models.TransactionTypes
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

class TransactionMapperTest {
    @Test
    fun toDomain_WhenValidEntity_ReturnsDomainObject() {
        // Arrange
        val entity = TransactionEntity(
            id = 0L,
            date = LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            amount = 0.00,
            category = Categories.OTHER_INCOME.name,
            notes = null,
            transactionType = TransactionTypes.INCOMING,
            recurrenceInterval = null
        )
        // Act
        val domain: Transaction = entity.toDomain()
        // Assert
        assert(domain.id != null && domain.id == entity.id)
        assert(domain.date == Date(entity.date).toInstant().atZone(ZoneOffset.UTC).toLocalDateTime())
        assert(domain.amount == entity.amount)
        assert(domain.category.name == entity.category)
        assert(domain.notes.equals(entity.notes))
        assert(domain.transactionType == entity.transactionType)
        assert(domain.recurrence?.name == entity.recurrenceInterval)
    }

    @Test
    fun toEntity_WhenValidDomainObject_ReturnsEntity() {
        // Arrange
        val domain = Transaction(
            id = 0L,
            date = LocalDateTime.now(),
            amount = 0.00,
            category = Categories.OTHER_INCOME,
            notes = null,
            transactionType = TransactionTypes.INCOMING,
            recurrence = null
        )
        // Act
        val entity: TransactionEntity = domain.toEntity()
        // Assert
        assert(entity.id != null && domain.id == domain.id)
        assert(entity.date == domain.date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
        assert(entity.amount == domain.amount)
        assert(entity.category == domain.category.name)
        assert(entity.notes.equals(domain.notes))
        assert(entity.transactionType == domain.transactionType)
        assert(entity.recurrenceInterval == domain.recurrence?.name)
    }
}