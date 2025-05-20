package com.example.notesapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.ui.viewmodel.TaskViewModel
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    viewModel: TaskViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onDeleteConfirmed: () -> Unit
) {
    val task by viewModel.selected.observeAsState()
    var showConfirm by remember { mutableStateOf(false) }
    val formatter = remember {
        DateTimeFormatter
            .ofPattern("dd MMM yyyy", Locale.getDefault())
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = task?.name ?: "") },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = stringResource(R.string.back)
                            )
                        }
                    },
                    actions = {
                        if (task != null) {
                            IconButton(onClick = onEdit) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = stringResource(R.string.edit)
                                )
                            }
                            IconButton(onClick = { showConfirm = true }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete),
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
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
                task?.let { t ->
                    Text(
                        text = stringResource(R.string.label_name) + ": " + t.name,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(R.string.label_note) + ": " + (t.note ?: ""),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val createdText = Instant.ofEpochMilli(t.createdAt)
                        .atZone(ZoneId.systemDefault())
                        .format(formatter)
                    Text(
                        text = stringResource(R.string.created_at, createdText),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(Modifier.height(8.dp))
                    val dueText = Instant.ofEpochMilli(t.dueDate)
                        .atZone(ZoneId.systemDefault())
                        .format(formatter)
                    Text(
                        text = stringResource(R.string.due_date, dueText),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

        if (showConfirm && task != null) {
            AlertDialog(
                onDismissRequest = { showConfirm = false },
                title            = { Text(stringResource(R.string.confirm_title)) },
                text             = { Text(stringResource(R.string.confirm_delete)) },
                confirmButton    = {
                    TextButton(onClick = {
                        viewModel.selected.value?.let(viewModel::delete)
                        showConfirm = false
                        onDeleteConfirmed()
                    }) {
                        Text(stringResource(R.string.delete))
                    }
                },
                dismissButton    = {
                    TextButton(onClick = { showConfirm = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}
