package com.example.notesapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.notesapp.ui.screens.EditTaskScreen
import com.example.notesapp.ui.screens.TaskDetailScreen
import com.example.notesapp.ui.screens.TaskListScreen
import com.example.notesapp.ui.viewmodel.TaskViewModel

sealed class Screen(val route: String) {
    object List : Screen("task_list")
    object Detail : Screen("task_detail/{taskId}") {
        fun createRoute(id: Long) = "task_detail/$id"
    }
    object Edit : Screen("task_edit?taskId={taskId}") {
        fun createRoute(id: Long?) = id?.let { "task_edit?taskId=$it" } ?: "task_edit"
    }
}

@Composable
fun AppNavGraph(viewModel: TaskViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.List.route
    ) {
        composable(Screen.List.route) {
            TaskListScreen(
                viewModel = viewModel,
                onAdd = { navController.navigate(Screen.Edit.createRoute(null)) },
                onSelect = { navController.navigate(Screen.Detail.createRoute(it)) }
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("taskId") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("taskId") ?: 0L
            viewModel.select(id)
            TaskDetailScreen(
                viewModel = viewModel,
                onEdit = { navController.navigate(Screen.Edit.createRoute(id)) },
                onDelete = { viewModel.delete(viewModel.selected.value!!); navController.popBackStack() },
                onBack = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.Edit.route,
            arguments = listOf(navArgument("taskId") {
                type = NavType.LongType
                defaultValue = -1L
                nullable = true
            })
        ) { backStackEntry ->
            val idArg = backStackEntry.arguments?.getLong("taskId")
            val taskId = if (idArg != null && idArg >= 0L) idArg else null
            EditTaskScreen(
                viewModel = viewModel,
                taskId = taskId,
                onSave = { navController.popBackStack() },
                onCancel = { navController.popBackStack() }
            )
        }
    }
}