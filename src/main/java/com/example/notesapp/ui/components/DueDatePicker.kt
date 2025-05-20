package com.example.notesapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.notesapp.R
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import androidx.compose.material3.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DueDatePicker(
    selectedDateMillis: Long,
    onDateSelected: (Long) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val now = DateFormat.getDateInstance().format(Date(selectedDateMillis))



    OutlinedTextField(
        value = DateFormat.getDateInstance().format(Date(selectedDateMillis)),
        onValueChange = {},
        label = { (stringResource(R.string.due_date, now) ) },
        readOnly = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        if (event.changes.any { it.pressed }) {
                            showDatePicker = true
                            break
                        }
                    }
                }
            }
    )

    if (showDatePicker) {
        val endOfMillis = Calendar.getInstance().apply {
            set(2100, Calendar.JANUARY, 1)
        }.timeInMillis
        val todayMillis = Calendar.getInstance().timeInMillis
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)

        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDateMillis,
            initialDisplayedMonthMillis = selectedDateMillis,
            yearRange = currentYear..2100,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis in todayMillis..endOfMillis
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { onDateSelected(it) }
                    showDatePicker = false
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                modifier = Modifier
            )
        }
    }
}