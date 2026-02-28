package com.example.notesapp.data.database

import com.example.notesapp.data.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(private val dao: TaskDao) : TaskRepository {

    override val allTasks: Flow<List<Task>> = dao.getAll()

    override fun getTask(id: Long): Flow<Task?> = dao.getById(id)

    override suspend fun add(task: Task): Long = withContext(Dispatchers.IO) {
        dao.insert(task)
    }

    override suspend fun update(task: Task) = withContext(Dispatchers.IO) {
        dao.update(task)
    }

    override suspend fun delete(task: Task) = withContext(Dispatchers.IO) {
        dao.delete(task)
    }
}
