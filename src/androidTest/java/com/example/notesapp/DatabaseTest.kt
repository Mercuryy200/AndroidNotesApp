package com.example.notesapp

import android.content.Context

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.notesapp.data.database.TaskDao
import com.example.notesapp.data.database.TaskDatabase
import com.example.notesapp.data.model.Task
import kotlinx.coroutines.flow.count
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
        val tasks = taskDao.getAll()
        assertEquals(
            1,
            tasks.count()
        )
    }
}
