package com.example.notesapp.ui.screens.tasklist

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notesapp.data.database.Task
import com.example.notesapp.data.database.TaskRepository
import kotlinx.coroutines.launch

class TaskListViewModel(private val repo: TaskRepository) : ViewModel() {
    val tasks: LiveData<List<Task>> = repo.allTasks

    fun delete(task: Task) {
        viewModelScope.launch {
            repo.delete(task)
        }
    }
}