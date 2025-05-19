package com.example.notesapp.ui.screens.taskedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.database.Task
import com.example.notesapp.data.database.TaskRepository
import kotlinx.coroutines.launch

class TaskEditViewModel(private val repo: TaskRepository) : ViewModel() {

    fun save(task: Task, isNew: Boolean) {
        viewModelScope.launch {
            if (isNew) {
                repo.add(task)
            } else {
                repo.update(task)
            }
        }
    }
}