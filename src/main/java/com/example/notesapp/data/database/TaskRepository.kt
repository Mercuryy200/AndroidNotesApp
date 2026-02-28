package com.example.notesapp.data.database

import com.example.notesapp.data.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    val allTasks: Flow<List<Task>>
    fun getTask(id: Long): Flow<Task?>
    suspend fun add(task: Task): Long
    suspend fun update(task: Task)
    suspend fun delete(task: Task)
}
