package com.example.notesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.notesapp.R
import com.example.notesapp.ui.components.TaskItem
import com.example.notesapp.ui.viewmodel.TaskViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAdd: () -> Unit,
    onSelect: (Long) -> Unit
) {
    val tasks by viewModel.repo.allTasks.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(stringResource(R.string.task_list_title) )})
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
               Text( stringResource(R.string.empty_task_list))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(tasks) { task ->
                    TaskItem(
                        task = task,
                        onClick = { onSelect(task.id) },
                        onCheckedChange = { viewModel.update(task.copy(isCompleted = it)) }
                    )
                }
            }
        }
    }
}
