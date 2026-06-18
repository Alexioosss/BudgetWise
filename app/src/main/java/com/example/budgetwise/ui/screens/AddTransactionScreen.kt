package com.example.budgetwise.ui.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.budgetwise.ui.domain.models.Categories
import com.example.budgetwise.ui.domain.models.TransactionTypes
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(transactionType: String?) {
    val type = when(transactionType) {
        "incoming" -> "Income"
        "outgoing" -> "Expense"
        else -> "Unknown"
    }

    if(type == "Unknown") {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Invalid transaction type. Please select a valid type.",
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.error,
                textAlign = TextAlign.Center
            )
        }
        return
    }
    key(transactionType) {
        var newTransactionAmountText by remember { mutableStateOf("") }
        var selectedCategory by remember { mutableStateOf("") }
        var categoryExpanded by remember { mutableStateOf(false) }
        val categories = Categories.entries.filter { category ->
            when(transactionType) {
                "incoming" -> category.transactionTypes == TransactionTypes.INCOMING
                "outgoing" -> category.transactionTypes == TransactionTypes.OUTGOING
                else -> true
            }
        }
        val accentColor =
            if(transactionType == "incoming") colorScheme.primary else colorScheme.error
        var selectedDateMillis by remember { mutableStateOf<Long?>(null) }
        val datePickerState = rememberDatePickerState()
        fun formatDate(millis: Long?): String {
            if(millis == null) { return "Select transaction date" }
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            return formatter.format(Date(millis))
        }
        var showDatePicker by remember { mutableStateOf(false) }
        val currentTime = LocalTime.now()
        val timePickerState = rememberTimePickerState(
            initialHour = currentTime.hour,
            initialMinute = currentTime.minute,
            is24Hour = true
        )
        var showTimePicker by remember { mutableStateOf(false) }
        fun formatTime(hour: Int, minute: Int): String {
            val hourFormatted = hour.toString().padStart(2, '0')
            val minuteFormatted = minute.toString().padStart(2, '0')
            return "$hourFormatted:$minuteFormatted"
        }
        fun clearInputs() {
            newTransactionAmountText = ""
            selectedCategory = ""
            selectedDateMillis = null
        }
        val clearTransactionButtonEnabled = newTransactionAmountText.isNotEmpty()
                || selectedCategory.isNotEmpty()
                || selectedDateMillis != null
        fun saveTransaction() {
        }
        val saveTransactionButtonEnabled = newTransactionAmountText.isNotEmpty()
                && selectedCategory.isNotEmpty()
                && selectedDateMillis != null
        var transactionNotes by remember { mutableStateOf("") }
        var isTransactionRecurring by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Add New $type",
                style = MaterialTheme.typography.headlineLarge,
                color = colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(32.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.surfaceVariant)
                    .padding(vertical = 20.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = "Amount",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
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
                        value = newTransactionAmountText,
                        onValueChange = { input ->
                            val filtered = input.filter { it.isDigit() || it == '.' }
                            if (filtered.count { it == '.' } <= 1) {
                                newTransactionAmountText = filtered
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
                                if (newTransactionAmountText.isEmpty()) {
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
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it },
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
                            text = selectedCategory.ifEmpty { "Select a category" },
                            style = MaterialTheme.typography.bodyLarge,
                            color = if (selectedCategory.isEmpty())
                                colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                            else
                                colorScheme.onBackground
                        )
                    }
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = categoryExpanded,
                        modifier = Modifier.scale(1.3f))
                }
                ExposedDropdownMenu(
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false },
                    modifier = Modifier.heightIn(max = 320.dp)
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = category.label,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if(category.label == selectedCategory)
                                    colorScheme.primary else colorScheme.onBackground
                                )
                            },
                            onClick = {
                                selectedCategory = category.label
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.surfaceVariant)
                    .padding(12.dp)
            ) {
                Text(
                    text = "Transaction Date",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorScheme.surface)
                        .padding(16.dp)
                        .clickable { showDatePicker = true }
                ) {
                    Text(
                        text = formatDate(selectedDateMillis),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (selectedDateMillis == null)
                            colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        else colorScheme.onSurface
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(colorScheme.surfaceVariant)
                    .padding(12.dp)
            ) {
                Text(
                    text = "Transaction Time (optional)",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(colorScheme.surface)
                        .padding(16.dp)
                        .clickable { showTimePicker = true }
                ) {
                    Text(
                        text = formatTime(timePickerState.hour, timePickerState.minute),
                        style = MaterialTheme.typography.bodyLarge,
                        color = if(selectedDateMillis == null)
                            colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        else colorScheme.onSurface
                    )
                }
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
                Text(
                    text = "Notes (optional)",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicTextField(
                        value = transactionNotes,
                        onValueChange = { newValue ->
                            transactionNotes = newValue
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = false,
                        textStyle = MaterialTheme.typography.headlineLarge.copy(
                            color = accentColor,
                            textAlign = TextAlign.Start
                        ),
                        cursorBrush = SolidColor(accentColor),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            Box {
                                if (newTransactionAmountText.isEmpty()) {
                                    Text(
                                        text = "Enter any notes about the transaction here",
                                        style = MaterialTheme.typography.bodyMedium.copy(
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
                        checked = isTransactionRecurring,
                        onCheckedChange = { value -> isTransactionRecurring = value },
                    )
                    Text(
                        text = "Recurring Transaction",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onBackground
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { clearInputs() },
                enabled = clearTransactionButtonEnabled,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = if(clearTransactionButtonEnabled) colorScheme.onPrimary
                    else colorScheme.onPrimary.copy(alpha = 0.9f)
                )
            ) {
                Text(
                    text = "Clear fields",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if(clearTransactionButtonEnabled) colorScheme.surface
                    else colorScheme.surface.copy(alpha = 0.9f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Button(
                onClick = { saveTransaction() },
                enabled = saveTransactionButtonEnabled,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorScheme.primary,
                    contentColor = if(clearTransactionButtonEnabled) colorScheme.onPrimary
                    else colorScheme.onPrimary.copy(alpha = 0.9f)
                )
            ) {
                Text(
                    text = "Save $type",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if(clearTransactionButtonEnabled) colorScheme.surface
                    else colorScheme.surface.copy(alpha = 0.9f)
                )
            }

            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Text(
                            text = "OK",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    selectedDateMillis = datePickerState.selectedDateMillis
                                    showDatePicker = false
                                }
                        )
                    },
                    dismissButton = {
                        Text(
                            text = "Cancel",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    showDatePicker = false
                                }
                        )
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
            if(showTimePicker) {
                TimePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        Text(
                            text = "0K",
                            modifier = Modifier.padding(16.dp)
                                .clickable {
                                    showTimePicker = false
                                }
                        )
                    },
                    dismissButton = {
                        Text(
                            text = "Cancel",
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    showTimePicker = false
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
    }
}