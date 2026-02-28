package com.example.notesapp.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import com.example.notesapp.R
import com.example.notesapp.data.model.Task
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
@Composable
 fun TaskItem(
    task: Task,
    onClick: () -> Unit,
    onCheckedChange: (Boolean) -> Unit
) {
    val targetAlpha = if (task.isCompleted) 0.5f else 1f
    val rowAlpha by animateFloatAsState(targetAlpha)

    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
    val formattedDate = Instant.ofEpochMilli(task.dueDate)
        .atZone(ZoneId.systemDefault())
        .format(formatter)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(rowAlpha)
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
                text = stringResource(R.string.due_date, formattedDate),
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        PriorityChip(priority = task.priority)
    }
}