package com.example.notesapp.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.notesapp.R
import com.example.notesapp.data.model.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdown(
    selected: Priority,
    onSelect: (Priority) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            value = selected.name,
            onValueChange = { /* no-op */ },
            readOnly = true,
            label = { Text(stringResource(R.string.label_priority)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()          // <-- anchor the dropdown to this TextField
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp) // <-- cap the height so inner list can scroll
        ) {
            Priority.values().forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.name) },
                    onClick = {
                        onSelect(priority)
                        expanded = false
                    }
                )
            }
        }
    }
}
