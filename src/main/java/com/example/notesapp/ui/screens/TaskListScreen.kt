package com.example.notesapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.notesapp.data.model.Task
import com.example.notesapp.ui.components.PriorityChip
import com.example.notesapp.ui.viewmodel.TaskViewModel
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAdd: () -> Unit,
    onSelect: (Long) -> Unit
) {
    // Observe LiveData<Array<Task>> from ViewModel
    val tasksArray by viewModel.allTasksLive.observeAsState(initial = emptyArray())
    val tasks = tasksArray.toList()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Mes tâches") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Ajouter tâche")
            }
        }
    ) { paddingValues ->
        if (tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text("Aucune tâche disponible.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(tasks) { task ->
                    TaskRow(
                        task = task,
                        onClick = { onSelect(task.id) },
                        onCheckedChange = { viewModel.update(task.copy(isCompleted = it)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun TaskRow(
    task: Task,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm")
    val formattedDate = Instant.ofEpochMilli(task.dueDate)
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = task.isCompleted,
            onCheckedChange = onCheckedChange
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable(onClick = onClick)
        ) {
            Text(
                text = task.name,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "À faire pour: $formattedDate",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        PriorityChip(priority = task.priority)
    }
}