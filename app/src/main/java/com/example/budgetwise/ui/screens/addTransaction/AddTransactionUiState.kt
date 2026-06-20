package com.example.budgetwise.ui.screens.addTransaction

import com.example.budgetwise.ui.domain.models.TransactionRecurrence

data class AddTransactionUiState(
    val amount: String = "",
    val category: String = "",
    val dateMillis: Long? = null,
    val hour: Int = 0,
    val minute: Int = 0,
    val notes: String = "",
    val isRecurring: Boolean = false,
    val recurringDateMillis: Long? = null,
    val isDatePickerOpen: Boolean = false,
    val isTimePickerOpen: Boolean = false,
    val isRecurringDatePickerOpen: Boolean = false,
    val isCategoryExpanded: Boolean = false,
    val isSuccessful: Boolean = false,
    val recurrenceInterval: TransactionRecurrence? = null,
    val isRecurrenceIntervalExpanded: Boolean = false
) {
    val isSaveEnabled: Boolean
        get() = amount.isNotBlank() && dateMillis != null

    val isClearEnabled: Boolean
        get() = amount.isNotBlank() || category.isNotBlank()
                || dateMillis != null || notes.isNotBlank()
}