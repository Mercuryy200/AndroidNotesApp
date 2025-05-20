package com.example.notesapp.data.database

import com.example.notesapp.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TaskRepository(private val dao: TaskDao) {

    suspend fun getAllTasks(): Array<Task> = withContext(Dispatchers.IO) {
        dao.getAll()
    }

    fun getTask(id: Long): Flow<Task?> = dao.getById(id)
    suspend fun add(task: Task) = withContext(Dispatchers.IO) {
        dao.insert(task)
    }
    suspend fun update(task: Task) = withContext(Dispatchers.IO) {
        dao.update(task)
    }
    suspend fun delete(task: Task) = withContext(Dispatchers.IO) {
        dao.delete(task)
    }
}