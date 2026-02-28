package com.example.notesapp.data.database

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.notesapp.data.model.Priority
import com.example.notesapp.data.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Seeds the database with mock tasks whenever the table is empty.
 * Using [onOpen] (rather than [onCreate]) ensures seeding works even when
 * the database file already existed from a previous install.
 */
class MockTasksCallback(private val dao: () -> TaskDao) : RoomDatabase.Callback() {

    override fun onOpen(db: SupportSQLiteDatabase) {
        super.onOpen(db)
        CoroutineScope(Dispatchers.IO).launch {
            val cursor = db.query("SELECT COUNT(*) FROM tasks", emptyArray())
            val count = if (cursor.moveToFirst()) cursor.getInt(0) else 0
            cursor.close()
            if (count == 0) {
                dao().insertAll(mockTasks())
            }
        }
    }

    private fun now() = System.currentTimeMillis()
    private fun days(n: Long) = TimeUnit.DAYS.toMillis(n)

    private fun mockTasks() = listOf(
        Task(
            name = "Submit project report",
            priority = Priority.HIGH,
            note = "Include charts from last week's data and executive summary.",
            dueDate = now() + days(1),
            isCompleted = false
        ),
        Task(
            name = "Team stand-up meeting",
            priority = Priority.MEDIUM,
            note = "Prepare bullet points: blockers, progress, next steps.",
            dueDate = now() + days(2),
            isCompleted = false
        ),
        Task(
            name = "Buy groceries",
            priority = Priority.LOW,
            note = "Milk, eggs, bread, tomatoes, olive oil.",
            dueDate = now() + days(1),
            isCompleted = false
        ),
        Task(
            name = "Review pull request",
            priority = Priority.HIGH,
            note = "Check the authentication refactor branch — focus on token refresh logic.",
            dueDate = now() + days(3),
            isCompleted = false
        ),
        Task(
            name = "Book dentist appointment",
            priority = Priority.MEDIUM,
            note = null,
            dueDate = now() + days(7),
            isCompleted = false
        ),
        Task(
            name = "Read Kotlin coroutines chapter",
            priority = Priority.LOW,
            note = "Chapters 9–11 of the Kotlin in Action book.",
            dueDate = now() + days(5),
            isCompleted = false
        ),
        Task(
            name = "Pay electricity bill",
            priority = Priority.HIGH,
            note = null,
            dueDate = now() - days(1),   // overdue — will show in red
            isCompleted = false
        ),
        Task(
            name = "Call mom",
            priority = Priority.MEDIUM,
            note = "Ask about weekend plans.",
            dueDate = now() + days(2),
            isCompleted = true
        )
    )
}
