package com.example.notesapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notesapp.data.model.Priority
@Composable
fun PriorityChip(priority: Priority) {
    val backgroundColor = when (priority) {
        Priority.BAS -> Color(0xFF81C784)
        Priority.MOYEN -> Color(0xFFFFF176)
        Priority.ÉLEVÉ -> Color(0xFFE57373)
    }
    Text(
        text = stringResource(priority.labelRes),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 8.dp, vertical = 4.dp),
        color = Color.Black
    )
}
