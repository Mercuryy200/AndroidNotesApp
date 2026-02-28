package com.example.notesapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.data.model.Priority
import com.example.notesapp.data.preferences.SortOrder
import com.example.notesapp.ui.components.TaskItem
import com.example.notesapp.ui.viewmodel.TaskUiState
import com.example.notesapp.ui.viewmodel.TaskViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onAdd: () -> Unit,
    onSelect: (Long) -> Unit
) {
    val uiState by viewModel.filteredUiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val filterPriority by viewModel.filterPriority.collectAsState()
    val showCompleted by viewModel.showCompleted.collectAsState()
    val userPrefs by viewModel.userPreferences.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(R.string.task_list_title)) },
                actions = {
                    IconButton(onClick = { viewModel.toggleSortOrder() }) {
                        Icon(
                            imageVector = if (userPrefs.sortOrder == SortOrder.BY_DUE_DATE_ASC)
                                Icons.Default.KeyboardArrowUp
                            else
                                Icons.Default.KeyboardArrowDown,
                            contentDescription = stringResource(R.string.sort_order_toggle)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAdd) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_task)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("search_bar")
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = !showCompleted,
                    onClick = { viewModel.setShowCompleted(!showCompleted) },
                    label = { Text(stringResource(R.string.filter_hide_completed)) }
                )
                Priority.entries.forEach { priority ->
                    FilterChip(
                        selected = filterPriority == priority,
                        onClick = {
                            viewModel.setFilterPriority(
                                if (filterPriority == priority) null else priority
                            )
                        },
                        label = { Text(stringResource(priority.labelRes)) }
                    )
                }
            }

            when (val state = uiState) {
                is TaskUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is TaskUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(state.message)
                    }
                }
                is TaskUiState.Success -> {
                    if (state.tasks.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(stringResource(R.string.empty_task_list))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .testTag("task_list")
                        ) {
                            items(state.tasks) { task ->
                                TaskItem(
                                    task = task,
                                    onClick = { onSelect(task.id) },
                                    onCheckedChange = {
                                        viewModel.update(task.copy(isCompleted = it))
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
