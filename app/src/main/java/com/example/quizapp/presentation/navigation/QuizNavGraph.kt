package com.example.quizapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.presentation.screens.CategoriesScreen
import com.example.quizapp.presentation.screens.QuizScreen

@Composable
fun QuizNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "categories"
    ) {
        composable("categories") {
            CategoriesScreen(navController = navController)
        }
        composable("quiz/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            QuizScreen(
                category = category,
                navController = navController
            )
        }
    }
}

