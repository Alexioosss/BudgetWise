package com.example.budgetwise.ui.domain.models

enum class TransactionTypes(val label: String) {
    INCOMING(label = "Income"),
    OUTGOING(label = "Expense")
}
enum class Categories(val transactionType: TransactionTypes, val label: String) {
    // Income
    SALARY(transactionType = TransactionTypes.INCOMING, label = "Salary"),
    FREELANCE(transactionType = TransactionTypes.INCOMING, label = "Freelance"),
    INVESTMENTS(transactionType = TransactionTypes.INCOMING, label = "Investments"),
    BENEFITS(transactionType = TransactionTypes.INCOMING, label = "Benefits"),
    GIFTS_RECEIVED(transactionType = TransactionTypes.INCOMING, label = "Gifts Received"),
    OTHER_INCOME(transactionType = TransactionTypes.INCOMING, label = "Other Income"),

    // Expenses
    HOUSING(transactionType = TransactionTypes.OUTGOING, label = "Housing & Rent"),
    FOOD(transactionType = TransactionTypes.OUTGOING, label = "Food & Dining"),
    TRANSPORT(transactionType = TransactionTypes.OUTGOING, label = "Transport"),
    VEHICLES(transactionType = TransactionTypes.OUTGOING, label = "Vehicles"),
    HEALTH(transactionType = TransactionTypes.OUTGOING, label = "Health & Medical"),
    GYM(transactionType = TransactionTypes.OUTGOING, label = "Gym & Fitness"),
    ENTERTAINMENT(transactionType = TransactionTypes.OUTGOING, label = "Entertainment & Cinema"),
    SHOPPING(transactionType = TransactionTypes.OUTGOING, label = "Shopping & Clothing"),
    SAVINGS(transactionType = TransactionTypes.OUTGOING, label = "Savings"),
    EDUCATION(transactionType = TransactionTypes.OUTGOING, label = "Education"),
    SUBSCRIPTIONS(transactionType = TransactionTypes.OUTGOING, label = "Subscriptions"),
    OTHER_EXPENSE(transactionType = TransactionTypes.OUTGOING, label = "Other Expense")
}