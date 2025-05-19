package com.example.notesapp.data.database

import Task
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks ORDER BY due_date ASC")
    fun getAll(): LiveData<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id LIMIT 1")
    fun getById(id: Long): LiveData<Task?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task): Long

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}