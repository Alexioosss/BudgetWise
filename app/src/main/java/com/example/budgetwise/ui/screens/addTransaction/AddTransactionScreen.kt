package com.example.budgetwise.ui.screens.addTransaction

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Label
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.budgetwise.ui.components.FormInputCard
import com.example.budgetwise.ui.components.PageHeader
import com.example.budgetwise.ui.components.ReusableButton
import com.example.budgetwise.ui.displayToast
import com.example.budgetwise.ui.domain.models.TransactionRecurrence
import com.example.budgetwise.ui.domain.models.TransactionTypes
import com.example.budgetwise.ui.viewmodels.TransactionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    transactionType: String?,
    viewModel: TransactionsViewModel = hiltViewModel(),
    context: Context
) {
    val type = when(transactionType) {
        "incoming" -> TransactionTypes.INCOMING
        "outgoing" -> TransactionTypes.OUTGOING
        else -> TransactionTypes.OUTGOING
    }

    LaunchedEffect(type) {
        viewModel.setTransactionType(type)
    }

    val accentColor = if(transactionType == "incoming") colorScheme.primary else colorScheme.error
    val categories = viewModel.getCategories()

    val state = viewModel.uiState.collectAsState().value
    val initialMillis = state.dateMillis

    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = initialMillis)
    val timePickerState = rememberTimePickerState()

    if(state.isSuccessful) {
        displayToast(context, "Transaction recorded successfully!")
        viewModel.clearFields()
    }

    PageHeader(
        title = "Add New ${type.label}",
        subtitle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(32.dp))
            FormInputCard(
                label = "Amount",
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "£",
                        style = MaterialTheme.typography.headlineLarge,
                        color = colorScheme.onBackground
                    )
                    Spacer(Modifier.width(4.dp))
                    BasicTextField(
                        value = state.amount,
                        onValueChange = { input ->
                            val filteredInput = input.filter { it.isDigit() || it == '.' }
                            if (filteredInput.count { it == '.' } <= 1) {
                                viewModel.updateAmount(filteredInput)
                            }
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        textStyle = MaterialTheme.typography.headlineLarge.copy(
                            color = accentColor,
                            textAlign = TextAlign.Start
                        ),
                        cursorBrush = SolidColor(accentColor),
                        modifier = Modifier.width(IntrinsicSize.Min)
                            .widthIn(min = 48.dp, max = 200.dp),
                        decorationBox = { innerTextField ->
                            Box {
                                if (state.amount.isEmpty()) {
                                    Text(
                                        text = "0.00",
                                        style = MaterialTheme.typography.headlineLarge.copy(
                                            color = colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                            textAlign = TextAlign.Start
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
            ExposedDropdownMenuBox(
                expanded = state.isCategoryExpanded,
                onExpandedChange = { viewModel.toggleCategoryExpanded() },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.surfaceVariant)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Label,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(35.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.headlineMedium,
                            color = colorScheme.onBackground,
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.category.lowercase()
                                .replace("_", " ")
                                .replaceFirstChar { it.uppercase() }
                                .ifEmpty { "Select a category" },
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (state.category.isEmpty())
                                colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            else colorScheme.onBackground
                        )
                    }
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = state.isCategoryExpanded,
                        modifier = Modifier.scale(1.3f)
                    )
                }
                ExposedDropdownMenu(
                    expanded = state.isCategoryExpanded,
                    onDismissRequest = { viewModel.toggleCategoryExpanded() },
                    modifier = Modifier.heightIn(max = 320.dp)
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = category.name.lowercase()
                                        .replace("_", " ")
                                        .replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if(category.name == state.category)
                                        colorScheme.primary else colorScheme.onBackground
                                )
                            },
                            onClick = {
                                viewModel.updateCategory(category.name)
                                viewModel.toggleCategoryExpanded()
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FormInputCard(
                label = "Transaction Date",
                onClick = { viewModel.toggleDatePicker() },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text(
                        text = viewModel.formattedTransactionDate(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (state.dateMillis == null)
                            colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        else colorScheme.onSurface
                    )
                }
                if(state.isDatePickerOpen) {
                    DatePickerDialog(
                        onDismissRequest = { viewModel.toggleDatePicker() },
                        confirmButton = {
                            Text(
                                text = "OK",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        viewModel.onDateSelected(
                                            datePickerState.selectedDateMillis)
                                    }
                            )
                        },
                        dismissButton = {
                            Text(
                                text = "Cancel",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        viewModel.toggleDatePicker()
                                    }
                            )
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FormInputCard(
                label = "Transaction Time (optional)",
                onClick = { viewModel.toggleTimePicker() },
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    Text(
                        text = viewModel.formattedTransactionTime(),
                        style = MaterialTheme.typography.bodyLarge,
                        color = colorScheme.onSurface,
                    )
                }
                if(state.isTimePickerOpen) {
                    TimePickerDialog(
                        onDismissRequest = { viewModel.toggleTimePicker() },
                        confirmButton = {
                            Text(
                                text = "OK",
                                modifier = Modifier.padding(16.dp)
                                    .clickable {
                                        viewModel.updateTime(
                                            timePickerState.hour,
                                            timePickerState.minute
                                        )
                                        viewModel.toggleTimePicker()
                                    }
                            )
                        },
                        dismissButton = {
                            Text(
                                text = "Cancel",
                                modifier = Modifier
                                    .padding(16.dp)
                                    .clickable {
                                        viewModel.toggleTimePicker()
                                    }
                            )
                        },
                        title = {
                            Text("Select time")
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .padding(16.dp)
                        ) {
                            TimePicker(
                                state = timePickerState,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            FormInputCard(
                label = "Notes (optional)",
                modifier = Modifier.fillMaxWidth(0.9f)
            ) {
                BasicTextField(
                    value = state.notes,
                    onValueChange = { newValue ->
                        viewModel.updateNotes(newValue)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = false,
                    textStyle = MaterialTheme.typography.headlineMedium.copy(
                        color = accentColor,
                        textAlign = TextAlign.Start
                    ),
                    cursorBrush = SolidColor(accentColor),
                    modifier = Modifier.fillMaxWidth(),
                    decorationBox = { innerTextField ->
                        Box {
                            if (state.notes.isEmpty()) {
                                Text(
                                    text = "Enter any notes about the transaction here",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                        textAlign = TextAlign.Start
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.surfaceVariant)
                    .padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.isRecurring,
                        onCheckedChange = { value ->
                            viewModel.updateRecurringState(value)
                        },
                    )
                    Text(
                        text = "Recurring transaction?",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (state.isRecurring) {
                FormInputCard(
                    label = "Recurrence Interval",
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = state.isRecurrenceIntervalExpanded,
                        onExpandedChange = { viewModel.toggleRecurrenceIntervalExpanded() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(colorScheme.surfaceVariant)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .padding(vertical = 16.dp, horizontal = 16.dp)
                        ) {
                            Text(
                                text = state.recurrenceInterval?.name ?: "Select interval",
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (state.recurrenceInterval == null)
                                    colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                else colorScheme.onSurface
                            )
                        }
                        ExposedDropdownMenu(
                            expanded = state.isRecurrenceIntervalExpanded,
                            onDismissRequest = { viewModel.toggleRecurrenceIntervalExpanded() }
                        ) {
                            TransactionRecurrence.entries.forEach { interval ->
                                DropdownMenuItem(
                                    text = { Text(interval.name) },
                                    onClick = {
                                        viewModel.updateRecurrenceInterval(interval)
                                        viewModel.toggleRecurrenceIntervalExpanded()
                                    }
                                )
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            ReusableButton(
                text = "Clear Fields",
                enabled = state.isClearEnabled,
                containerColour = colorScheme.primary,
                contentColour = colorScheme.onPrimary,
                textColour = colorScheme.surface,
                onClick = { viewModel.clearFields() }
            )
            Spacer(modifier = Modifier.height(4.dp))
            ReusableButton(
                text = "Record Transaction",
                enabled = state.isSaveEnabled,
                containerColour = colorScheme.primary,
                contentColour = colorScheme.onPrimary,
                textColour = colorScheme.surface,
                onClick = { viewModel.addTransaction() }
            )
        }
    }
}