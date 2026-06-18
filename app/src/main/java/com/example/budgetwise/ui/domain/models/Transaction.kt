package com.example.budgetwise.ui.domain.models

import java.time.LocalDateTime

class Transaction(
    val id: Long,
    val date: LocalDateTime,
    val amount: Double,
    val category: Categories,
    val notes: String? = null,
    val transactionType: TransactionTypes,
    val recurringDate: LocalDateTime?
) {
    init {
        require(amount >= 0) { "Transaction amount cannot be negative" }
    }

    override fun equals(other: Any?): Boolean {
        if(this === other) return true
        if(javaClass != other?.javaClass) return false

        other as Transaction
        return id == other.id
    }

    override fun hashCode(): Int {
        return id.toInt()
    }

    override fun toString(): String {
        return "Transaction(id=$id, date=$date, amount=$amount, " +
                "category=$category, notes=$notes, transactionType=${transactionType.name}" +
                if(recurringDate != null) ", recurringDate=$recurringDate)" else ")"
    }
}