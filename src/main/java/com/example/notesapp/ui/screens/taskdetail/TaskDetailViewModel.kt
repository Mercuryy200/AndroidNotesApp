package com.example.notesapp.ui.screens.taskdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.notesapp.data.database.Task
import com.example.notesapp.data.database.TaskRepository

class TaskDetailViewModel(private val repo: TaskRepository) : ViewModel() {
    fun getTask(id: Long): LiveData<Task?> = repo.getTask(id)
}