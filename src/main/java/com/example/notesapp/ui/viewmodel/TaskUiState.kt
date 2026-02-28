package com.example.notesapp.ui.viewmodel

import com.example.notesapp.data.model.Task

sealed class TaskUiState {
    object Loading : TaskUiState()
    data class Success(val tasks: List<Task>) : TaskUiState()
    data class Error(val message: String) : TaskUiState()
}
