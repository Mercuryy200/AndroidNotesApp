package com.example.notesapp.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.notesapp.ui.screens.tasklist.TaskListScreen
import com.example.notesapp.ui.screens.taskdetail.TaskDetailScreen
import com.example.notesapp.ui.screens.taskedit.TaskEditScreen

@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = "task_list") {
        composable("task_list") {
            TaskListScreen(navController)
        }
        composable("task_detail/{taskId}") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
            TaskDetailScreen(navController, taskId)
        }
        composable("task_edit/{taskId}?") { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId")?.toLongOrNull()
            TaskEditScreen(navController, taskId)
        }
    }
}