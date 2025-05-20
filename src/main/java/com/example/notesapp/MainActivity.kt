package com.example.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavGraph
import com.example.notesapp.ui.theme.NotesAppTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.example.notesapp.data.database.TaskDatabase
import com.example.notesapp.data.database.TaskRepository
import com.example.notesapp.navigation.AppNavGraph
import com.example.notesapp.ui.viewmodel.TaskViewModel
import com.example.notesapp.ui.viewmodel.TaskViewModelFactory
import com.jakewharton.threetenabp.AndroidThreeTen

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: TaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidThreeTen.init(this);
        val dao = TaskDatabase.getInstance(applicationContext).taskDao()
        val repo = TaskRepository(dao)
        viewModel = ViewModelProvider(this, TaskViewModelFactory(repo))[TaskViewModel::class.java]
        enableEdgeToEdge()
        setContent {
            NotesAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color =MaterialTheme.colorScheme.background
                ){
                    AppNavGraph(viewModel = viewModel)
                }
            }
        }
    }
}
