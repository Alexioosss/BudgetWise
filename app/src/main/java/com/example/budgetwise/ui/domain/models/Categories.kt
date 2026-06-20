package com.example.budgetwise.ui.domain.models

enum class TransactionTypes(val label: String) {
    INCOMING("Income"),
    OUTGOING("Expense")
}
enum class Categories(val transactionType: TransactionTypes, val label: String) {
    // Income
    SALARY(TransactionTypes.INCOMING, "Salary"),
    FREELANCE(TransactionTypes.INCOMING, "Freelance"),
    INVESTMENTS(TransactionTypes.INCOMING, "Investments"),
    BENEFITS(TransactionTypes.INCOMING, "Benefits"),
    GIFTS_RECEIVED(TransactionTypes.INCOMING, "Gifts Received"),
    OTHER_INCOME(TransactionTypes.INCOMING, "Other Income"),

    // Expenses
    HOUSING(TransactionTypes.OUTGOING, "Housing & Rent"),
    FOOD(TransactionTypes.OUTGOING, "Food & Dining"),
    TRANSPORT(TransactionTypes.OUTGOING, "Transport"),
    VEHICLES(TransactionTypes.OUTGOING, "Vehicles"),
    HEALTH(TransactionTypes.OUTGOING, "Health & Medical"),
    GYM(TransactionTypes.OUTGOING, "Gym & Fitness"),
    ENTERTAINMENT(TransactionTypes.OUTGOING, "Entertainment & Cinema"),
    SHOPPING(TransactionTypes.OUTGOING, "Shopping & Clothing"),
    SAVINGS(TransactionTypes.OUTGOING, "Savings"),
    EDUCATION(TransactionTypes.OUTGOING, "Education"),
    SUBSCRIPTIONS(TransactionTypes.OUTGOING, "Subscriptions"),
    OTHER_EXPENSE(TransactionTypes.OUTGOING, "Other Expense")
}