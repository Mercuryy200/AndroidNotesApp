package com.example.notesapp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.data.model.Priority
import com.example.notesapp.data.model.Task
import com.example.notesapp.ui.components.DueDatePicker
import com.example.notesapp.ui.components.PriorityDropdown
import com.example.notesapp.ui.viewmodel.TaskViewModel


@SuppressLint("AutoboxingStateValueProperty")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    taskId: Long?,
    viewModel: TaskViewModel,
    onSave: () -> Unit,
    onCancel: () -> Unit,
) {
    val isNew = taskId == null

    val existing by viewModel.selected.observeAsState()

    val nameState     = rememberSaveable(taskId) { mutableStateOf("") }
    val noteState     = rememberSaveable(taskId) { mutableStateOf("") }
    val dueDateState  = rememberSaveable(taskId) { mutableLongStateOf(System.currentTimeMillis()) }
    val priorityState = rememberSaveable(taskId) { mutableStateOf(Priority.MOYEN) }


    LaunchedEffect(taskId) {
        if (!isNew && taskId != null) {
            viewModel.select(taskId)
        }
    }
    LaunchedEffect(existing) {
        existing?.let { task ->
            nameState.value     = task.name
            noteState.value     = task.note ?: ""
            dueDateState.longValue  = task.dueDate
            priorityState.value = task.priority
        }
    }

    Box(modifier = Modifier.fillMaxSize(),    contentAlignment = Alignment.Center ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            stringResource(
                                if (isNew) R.string.new_task
                                else        R.string.edit_task
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    label = { Text(stringResource(R.string.label_name)) },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = noteState.value,
                    onValueChange = { noteState.value = it },
                    label = { Text(stringResource(R.string.label_note)) },
                    modifier = Modifier.fillMaxWidth().height(100.dp)
                )

                DueDatePicker(
                    selectedDateMillis = dueDateState.value,
                    onDateSelected     = { dueDateState.value = it }
                )

                PriorityDropdown(
                    selected = priorityState.value,
                    onSelect = { priorityState.value = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row (horizontalArrangement = Arrangement.Absolute.SpaceEvenly){
                    Button(
                        onClick = {
                            val task = Task(
                                id       = existing?.id ?: 0L,
                                name     = nameState.value,
                                note     = noteState.value,
                                dueDate  = dueDateState.value,
                                priority = priorityState.value
                            )
                            if (isNew) viewModel.add(task) else viewModel.update(task)
                            onSave()
                        },
                        enabled = nameState.value.isNotBlank()
                    ) {
                        Text(stringResource(R.string.save))
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(onClick = onCancel) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        }

    }

}