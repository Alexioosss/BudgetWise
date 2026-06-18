package com.example.budgetwise.data.mappers

import com.example.budgetwise.data.models.TransactionEntity
import com.example.budgetwise.ui.domain.models.Categories
import com.example.budgetwise.ui.domain.models.Transaction
import java.time.ZoneOffset
import java.util.Date

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = this.id,
        date = Date(this.date).toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(),
        amount = this.amount,
        category = Categories.valueOf(this.category),
        notes = this.notes,
        transactionType = this.transactionType,
        recurringDate = if(this.recurringDate != null) {
                Date(this.recurringDate).toInstant().atZone(ZoneOffset.UTC).toLocalDateTime()
        } else null
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        date = this.date.atZone(ZoneOffset.UTC).toInstant().toEpochMilli(),
        amount = this.amount,
        category = this.category.name,
        notes = this.notes,
        transactionType = this.transactionType,
        recurringDate = this.recurringDate?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    )
}