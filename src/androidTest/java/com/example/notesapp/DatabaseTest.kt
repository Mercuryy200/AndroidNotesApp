package com.example.notesapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.notesapp.data.database.TaskDao
import com.example.notesapp.data.database.TaskDatabase
import com.example.notesapp.data.model.Task
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class DatabaseTest {
    private lateinit var taskDao: TaskDao
    private lateinit var db: TaskDatabase

    private val task1 = Task(
        id = 1, name = "Faire devoir",
        note = "Faire le devoir de math"
    )

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        db = Room.inMemoryDatabaseBuilder(context, TaskDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        taskDao = db.taskDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() = db.close()

    @Test
    @Throws(Exception::class)
    fun insertAndRetrieve() = runBlocking {
        taskDao.insert(task1)
        val tasks = taskDao.getAll().first()
        assertEquals(1, tasks.size)
    }

    @Test
    fun updateTask() = runBlocking {
        taskDao.insert(task1)
        val updated = task1.copy(name = "Updated name")
        taskDao.update(updated)
        val tasks = taskDao.getAll().first()
        assertEquals("Updated name", tasks[0].name)
    }

    @Test
    fun deleteTask() = runBlocking {
        taskDao.insert(task1)
        taskDao.delete(task1)
        val tasks = taskDao.getAll().first()
        assertEquals(0, tasks.size)
    }

    @Test
    fun getById_returnsCorrectTask() = runBlocking {
        taskDao.insert(task1)
        val result = taskDao.getById(task1.id).first()
        assertEquals(task1.name, result?.name)
    }
}
