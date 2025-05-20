package com.example.notesapp.ui.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.notesapp.data.model.Task
import com.example.notesapp.data.database.TaskRepository

class TaskViewModel(private val repo: TaskRepository) : ViewModel() {

    val allTasksLive: LiveData<Array<Task>> =
        liveData(Dispatchers.IO) { emit(repo.getAllTasks()) }
    private val _selected = MutableLiveData<Task?>()
    val selected: LiveData<Task?> = _selected

    fun select(id: Long) {
        viewModelScope.launch {
            repo.getTask(id).collect { task ->
                _selected.value = task
            }
        }
    }

    fun add(task: Task) = viewModelScope.launch { repo.add(task) }
    fun update(task: Task) = viewModelScope.launch { repo.update(task) }
    fun delete(task: Task) = viewModelScope.launch { repo.delete(task) }
}