package com.example.notesapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.notesapp.data.database.TaskRepository
import com.example.notesapp.data.model.Priority
import com.example.notesapp.data.model.Task
import com.example.notesapp.data.preferences.SortOrder
import com.example.notesapp.data.preferences.UserPreferences
import com.example.notesapp.data.preferences.UserPreferencesRepository
import com.example.notesapp.workers.TaskReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repo: TaskRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _selected = MutableLiveData<Task?>()
    val selected: LiveData<Task?> = _selected

    private val _searchQuery = MutableStateFlow("")
    private val _filterPriority = MutableStateFlow<Priority?>(null)
    private val _showCompleted = MutableStateFlow(true)

    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    val filterPriority: StateFlow<Priority?> = _filterPriority.asStateFlow()
    val showCompleted: StateFlow<Boolean> = _showCompleted.asStateFlow()

    val userPreferences: StateFlow<UserPreferences> = userPreferencesRepository.userPreferences
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), UserPreferences())

    val filteredUiState: StateFlow<TaskUiState> = combine(
        repo.allTasks,
        _searchQuery,
        _filterPriority,
        _showCompleted,
        userPreferencesRepository.userPreferences
    ) { tasks, query, priority, showCompleted, prefs ->
        val filtered = tasks
            .filter { task ->
                (query.isBlank() ||
                    task.name.contains(query, ignoreCase = true) ||
                    (task.note?.contains(query, ignoreCase = true) == true)) &&
                (priority == null || task.priority == priority) &&
                (showCompleted || !task.isCompleted)
            }
            .let { list ->
                when (prefs.sortOrder) {
                    SortOrder.BY_DUE_DATE_ASC -> list.sortedBy { it.dueDate }
                    SortOrder.BY_DUE_DATE_DESC -> list.sortedByDescending { it.dueDate }
                    SortOrder.BY_PRIORITY -> list.sortedByDescending { it.priority.ordinal }
                }
            }
        TaskUiState.Success(filtered) as TaskUiState
    }
        .catch { emit(TaskUiState.Error(it.message ?: "Unknown error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), TaskUiState.Loading)

    fun select(id: Long) {
        viewModelScope.launch {
            repo.getTask(id).collect { task ->
                _selected.value = task
            }
        }
    }

    fun add(task: Task) = viewModelScope.launch {
        val id = repo.add(task)
        scheduleReminder(id, task.name, task.dueDate)
    }

    fun update(task: Task) = viewModelScope.launch {
        repo.update(task)
        cancelReminder(task.id)
        scheduleReminder(task.id, task.name, task.dueDate)
    }

    fun delete(task: Task) = viewModelScope.launch {
        repo.delete(task)
        cancelReminder(task.id)
    }

    fun setSearchQuery(query: String) { _searchQuery.value = query }
    fun setFilterPriority(priority: Priority?) { _filterPriority.value = priority }
    fun setShowCompleted(show: Boolean) { _showCompleted.value = show }

    fun toggleSortOrder() = viewModelScope.launch {
        userPreferencesRepository.toggleSortOrder()
    }

    private fun scheduleReminder(taskId: Long, taskName: String, dueDate: Long) {
        val delay = dueDate - System.currentTimeMillis() - TimeUnit.HOURS.toMillis(1)
        if (delay > 0) {
            val request = OneTimeWorkRequestBuilder<TaskReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .setInputData(
                    workDataOf(
                        TaskReminderWorker.KEY_TASK_ID to taskId,
                        TaskReminderWorker.KEY_TASK_NAME to taskName
                    )
                )
                .addTag("task_reminder_$taskId")
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                "task_reminder_$taskId",
                ExistingWorkPolicy.REPLACE,
                request
            )
        }
    }

    private fun cancelReminder(taskId: Long) {
        WorkManager.getInstance(context).cancelUniqueWork("task_reminder_$taskId")
    }
}
