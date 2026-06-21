package com.example.budgetwise.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.budgetwise.data.repositories.TransactionRepository
import com.example.budgetwise.ui.domain.models.Categories
import com.example.budgetwise.ui.domain.models.DailyOverview
import com.example.budgetwise.ui.domain.models.Transaction
import com.example.budgetwise.ui.domain.models.TransactionRecurrence
import com.example.budgetwise.ui.domain.models.TransactionTypes
import com.example.budgetwise.ui.screens.addTransaction.AddTransactionUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val repository: TransactionRepository): ViewModel() {
    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    private val _upcomingTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    private val _tomorrowTransactions = MutableStateFlow<List<Transaction>>(emptyList())
    private val _uiState = MutableStateFlow(AddTransactionUiState())
    private val _todayOverview = MutableStateFlow(DailyOverview())
    private var transactionType: TransactionTypes = TransactionTypes.OUTGOING
    val transactions: StateFlow<List<Transaction>> = _transactions
    val upcomingTransactions: StateFlow<List<Transaction>> = _upcomingTransactions
    val tomorrowTransactions: StateFlow<List<Transaction>> = _tomorrowTransactions
    val uiState: StateFlow<AddTransactionUiState> = _uiState
    val todayOverview: StateFlow<DailyOverview> = _todayOverview

    init { loadTransactions() }

    private fun loadTransactions() {
        viewModelScope.launch {
            val all: List<Transaction> = repository.getAll()
            _transactions.value = all

            _tomorrowTransactions.value = all.filter { t ->
                val tomorrowDate: LocalDate = LocalDate.now().plusDays(1)
                if(t.recurrenceInterval != null) {
                    val next: LocalDateTime = nextOccurrence(t.date, t.recurrenceInterval)
                    next.toLocalDate() == tomorrowDate
                } else {
                    t.date.toLocalDate() == tomorrowDate
                }
            }

            val now: LocalDateTime = LocalDateTime.now()
            val upcoming = all.mapNotNull { t->
                when {
                    t.recurrenceInterval != null -> {
                        val next = nextOccurrence(t.date, t.recurrenceInterval)
                        t.copy(date = next)
                    }
                    t.date.isAfter(now) -> { t }
                    else -> { null }
                }
            }
            _upcomingTransactions.value = upcoming
            loadDailyOverview()
        }
    }

    private fun nextOccurrence(original: LocalDateTime,
                               interval: TransactionRecurrence): LocalDateTime {
        var next: LocalDateTime = original
        val now: LocalDateTime = LocalDateTime.now()
        while(next.isBefore(now)) {
            next = when(interval) {
                TransactionRecurrence.DAILY -> next.plusDays(1)
                TransactionRecurrence.WEEKLY -> next.plusWeeks(1)
                TransactionRecurrence.MONTHLY -> next.plusMonths(1)
                TransactionRecurrence.YEARLY -> next.plusYears(1)
            }
        }
        return next
    }

    private fun loadDailyOverview() {
        viewModelScope.launch {
            val today: LocalDate = LocalDate.now()
            val start: Long = today.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
            val end: Long = today.plusDays(1).atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()

            val todayTransactions: List<Transaction> = repository.getByDateRange(start, end)
            val incomingAmount: Double = todayTransactions
                .filter { it.transactionType == TransactionTypes.INCOMING }
                .sumOf { it.amount }
            val outgoingAmount: Double = todayTransactions
                .filter { it.transactionType == TransactionTypes.OUTGOING }
                .sumOf { it.amount }
            val nextUpcoming: Transaction? = repository
                .getUpcoming(System.currentTimeMillis()).firstOrNull()

            _todayOverview.value = DailyOverview(
                incomingToday = incomingAmount,
                outgoingToday = outgoingAmount,
                netToday = incomingAmount - outgoingAmount,
                nextUpcoming = nextUpcoming
            )
        }
    }

    fun setTransactionType(type: TransactionTypes) {
        transactionType = type
    }
    fun getCategories(): List<Categories> {
        return Categories.entries.filter {
            it.transactionType == transactionType
        }
    }

    fun updateAmount(value: String) {
        _uiState.update { it.copy(amount = value) }
    }

    fun updateCategory(value: String) {
        _uiState.update { it.copy(category = value) }
    }

    fun updateNotes(value: String) {
        _uiState.update { it.copy(notes = value) }
    }

    fun updateDate(value: Long?) {
        _uiState.update { it.copy(dateMillis = value) }
    }

    fun onDateSelected(utcMillis: Long?) {
        if(utcMillis == null) { return }
        val localDate: LocalDate = Instant.ofEpochMilli(utcMillis)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
        val correctedMillis: Long = localDate
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
        updateDate(correctedMillis)
        toggleDatePicker()
    }

    fun updateTime(hour: Int, minute: Int) {
        _uiState.update { it.copy(hour = hour, minute = minute) }
    }

    fun updateRecurringState(value: Boolean) {
        _uiState.update { it.copy(isRecurring = value) }
    }

    fun updateRecurrenceInterval(value: TransactionRecurrence?) {
        _uiState.update { it.copy(recurrenceInterval = value) }
    }

    fun toggleRecurrenceIntervalExpanded() {
        _uiState.update { it.copy(isRecurrenceIntervalExpanded =
            !it.isRecurrenceIntervalExpanded) }
    }

    fun toggleCategoryExpanded() {
        _uiState.update { it.copy(isCategoryExpanded = !it.isCategoryExpanded) }
    }

    fun toggleDatePicker() {
        _uiState.update { it.copy(isDatePickerOpen = !it.isDatePickerOpen) }
    }

    fun toggleTimePicker() {
        _uiState.update { it.copy(isTimePickerOpen = !it.isTimePickerOpen) }
    }

    fun clearFields() {
        _uiState.value = AddTransactionUiState()
    }

    fun formattedTransactionDate(): String {
        return formatDate(uiState.value.dateMillis)
    }

    fun formattedTransactionTime(): String {
        return formatTime(uiState.value.hour, uiState.value.minute)
    }

    private fun formatDate(millis: Long?): String {
        if(millis == null) { return "Select transaction date" }
        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        return formatter.format(Date(millis))
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val hourFormatted: String = hour.toString().padStart(2, '0')
        val minuteFormatted: String = minute.toString().padStart(2, '0')
        return "$hourFormatted:$minuteFormatted"
    }

    fun addTransaction() {
        val state: AddTransactionUiState = _uiState.value
        if(state.dateMillis == null) { return }
        if(state.amount.isBlank()) { return }
        if(state.category.isBlank()) { return }

        viewModelScope.launch {
            val transaction = Transaction(
                id = null,
                date = Instant.ofEpochMilli(state.dateMillis)
                    .atZone(ZoneId.systemDefault())
                    .withHour(state.hour)
                    .withMinute(state.minute)
                    .toLocalDateTime(),
                amount = state.amount.toDouble(),
                category = Categories.valueOf(state.category),
                notes = state.notes,
                transactionType = transactionType,
                recurrenceInterval = state.recurrenceInterval
            )
            repository.insert(transaction)
            loadTransactions()
            _uiState.update { it.copy(isSuccessful = true) }
        }
    }

    fun deleteTransactionById(id: Long) {
        viewModelScope.launch {
            repository.deleteById(id)
            _transactions.update { currentList ->
                currentList.filterNot { it.id == id }
            }
        }
    }
}