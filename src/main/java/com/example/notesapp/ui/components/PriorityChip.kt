package com.example.notesapp.ui.components

import Priority
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PriorityChip(priority: Priority) {
    val text = when(priority) {
        Priority.BAS -> "Low"
        Priority.MOYEN -> "Medium"
        Priority.ÉLEVÉ -> "High"
    }
    AssistChip(
        onClick = {},
        label = { Text(text) },
        modifier = Modifier
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(4.dp)
    )
}
