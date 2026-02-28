package com.example.notesapp.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.work.WorkManager
import app.cash.turbine.test
import com.example.notesapp.data.FakeTaskRepository
import com.example.notesapp.data.model.Priority
import com.example.notesapp.data.model.Task
import com.example.notesapp.data.preferences.SortOrder
import com.example.notesapp.data.preferences.UserPreferences
import com.example.notesapp.data.preferences.UserPreferencesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class TaskViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeRepo: FakeTaskRepository
    private lateinit var mockPrefsRepo: UserPreferencesRepository
    private lateinit var viewModel: TaskViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkStatic(WorkManager::class)
        every { WorkManager.getInstance(any()) } returns mockk(relaxed = true)

        fakeRepo = FakeTaskRepository()
        mockPrefsRepo = mockk(relaxed = true) {
            every { userPreferences } returns flowOf(
                UserPreferences(sortOrder = SortOrder.BY_DUE_DATE_ASC)
            )
        }
        viewModel = TaskViewModel(fakeRepo, mockPrefsRepo, mockk(relaxed = true))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `initial state is Loading`() = runTest {
        // stateIn initialValue is Loading before any upstream emission
        val state = viewModel.filteredUiState.value
        assertTrue(state is TaskUiState.Loading)
    }

    @Test
    fun `add task updates state to Success with one task`() = runTest {
        viewModel.filteredUiState.test {
            awaitItem() // Loading

            viewModel.add(Task(name = "Test task", note = null))
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue(state is TaskUiState.Success)
            assertEquals(1, (state as TaskUiState.Success).tasks.size)
            assertEquals("Test task", state.tasks[0].name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `search filter narrows results by task name`() = runTest {
        fakeRepo.setTasks(
            listOf(
                Task(id = 1, name = "Meeting notes", note = null),
                Task(id = 2, name = "Shopping list", note = null)
            )
        )

        viewModel.filteredUiState.test {
            awaitItem() // Loading
            awaitItem() // Success with 2 tasks

            viewModel.setSearchQuery("Meeting")
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue(state is TaskUiState.Success)
            assertEquals(1, (state as TaskUiState.Success).tasks.size)
            assertEquals("Meeting notes", state.tasks[0].name)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `priority filter shows only matching priority tasks`() = runTest {
        fakeRepo.setTasks(
            listOf(
                Task(id = 1, name = "High task", note = null, priority = Priority.HIGH),
                Task(id = 2, name = "Low task", note = null, priority = Priority.LOW)
            )
        )

        viewModel.filteredUiState.test {
            awaitItem() // Loading
            awaitItem() // Success with 2 tasks

            viewModel.setFilterPriority(Priority.HIGH)
            advanceUntilIdle()

            val state = awaitItem()
            assertTrue(state is TaskUiState.Success)
            assertEquals(1, (state as TaskUiState.Success).tasks.size)
            assertEquals(Priority.HIGH, (state as TaskUiState.Success).tasks[0].priority)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
