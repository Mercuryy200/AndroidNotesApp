package com.example.notesapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.data.model.Priority
import com.example.notesapp.data.model.Task
import com.example.notesapp.ui.components.PriorityDropdown
import com.example.notesapp.ui.viewmodel.TaskViewModel
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTaskScreen(
    viewModel: TaskViewModel,
    taskId: Long?,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {
    LaunchedEffect(taskId) { taskId?.let { viewModel.select(it) } }
    val existing by viewModel.selected.observeAsState()

    var name by remember { mutableStateOf(existing?.name ?: "") }
    var note by remember { mutableStateOf(existing?.note ?: "") }
    var dueDate by remember { mutableStateOf(existing?.dueDate ?: System.currentTimeMillis()) }
    var priority by remember { mutableStateOf(existing?.priority ?: Priority.MOYEN) }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    if (taskId == null) stringResource(R.string.new_task)
                    else stringResource(R.string.edit_task)
                )
            })
        }
    ) { padding ->
        Column(modifier = Modifier
            .padding(padding)
            .padding(16.dp)) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.label_name)) }
            )
            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text(stringResource(R.string.label_note)) }
            )
            Text(
                text = stringResource(
                    R.string.due_date,
                    DateFormat.getDateTimeInstance().format(Date(dueDate))
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )
            PriorityDropdown(
                selected = priority,
                onSelect = { priority = it }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Button(
                    onClick = {
                        val task = Task(
                            id = existing?.id ?: 0L,
                            name = name,
                            note = note,
                            dueDate = dueDate,
                            priority = priority
                        )
                        viewModel.add(task)
                        onSave()
                    },
                    enabled = name.isNotBlank()
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