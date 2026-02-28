package com.example.notesapp.di

import android.content.Context
import androidx.room.Room
import com.example.notesapp.data.database.MockTasksCallback
import com.example.notesapp.data.database.TaskDao
import com.example.notesapp.data.database.TaskDatabase
import com.example.notesapp.data.database.TaskRepository
import com.example.notesapp.data.database.TaskRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TaskDatabase {
        lateinit var db: TaskDatabase
        db = Room.databaseBuilder(context, TaskDatabase::class.java, "tasks_db")
            .addCallback(MockTasksCallback { db.taskDao() })
            .build()
        return db
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: TaskDatabase): TaskDao = database.taskDao()

    @Provides
    @Singleton
    fun provideTaskRepository(dao: TaskDao): TaskRepository = TaskRepositoryImpl(dao)
}
