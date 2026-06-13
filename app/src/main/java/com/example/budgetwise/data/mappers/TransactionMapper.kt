package com.example.budgetwise.data.mappers

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.budgetwise.data.models.TransactionEntity
import com.example.budgetwise.ui.domain.model.Categories
import com.example.budgetwise.ui.domain.model.Transaction
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = this.id,
        date = Date(this.date).toInstant().atZone(ZoneOffset.UTC).toLocalDateTime(),
        amount = this.amount,
        category = Categories.valueOf(this.category),
        notes = this.notes
    )
}

fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        date = this.date.atZone(ZoneOffset.UTC).toInstant().toEpochMilli(),
        amount = this.amount,
        category = this.category.name,
        notes = this.notes
    )
}

object TransactionMapper {
    val entityToDomain: (TransactionEntity) -> Transaction = { entity -> entity.toDomain() }
    val domainToEntity: (Transaction) -> TransactionEntity = { domain -> domain.toEntity() }
}