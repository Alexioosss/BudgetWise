package com.example.budgetwise.ui.domain.models

data class DailyOverview(
    val incomingToday: Double = 0.0,
    val outgoingToday: Double = 0.0,
    val netToday: Double = 0.0,
    val nextUpcoming: Transaction? = null
)