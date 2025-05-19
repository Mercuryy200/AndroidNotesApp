package com.example.notesapp.data.database

import Converters
import Task
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Task::class], version = 1)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    companion object {
        @Volatile private var INSTANCE: TaskDatabase? = null
        fun getInstance(context: Context): TaskDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                TaskDatabase::class.java,
                "tasks_db"
            ).fallbackToDestructiveMigration().build().also { INSTANCE = it }
        }
    }
}