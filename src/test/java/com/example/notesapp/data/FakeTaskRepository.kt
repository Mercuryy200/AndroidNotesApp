package com.example.notesapp.data

import com.example.notesapp.data.database.TaskRepository
import com.example.notesapp.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeTaskRepository : TaskRepository {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    override val allTasks: Flow<List<Task>> = _tasks.asStateFlow()

    override fun getTask(id: Long): Flow<Task?> =
        _tasks.map { list -> list.find { it.id == id } }

    override suspend fun add(task: Task): Long {
        val newId = (_tasks.value.maxOfOrNull { it.id } ?: 0L) + 1L
        _tasks.value = _tasks.value + task.copy(id = newId)
        return newId
    }

    override suspend fun update(task: Task) {
        _tasks.value = _tasks.value.map { if (it.id == task.id) task else it }
    }

    override suspend fun delete(task: Task) {
        _tasks.value = _tasks.value.filter { it.id != task.id }
    }

    fun setTasks(tasks: List<Task>) {
        _tasks.value = tasks
    }
}
