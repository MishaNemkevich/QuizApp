package com.example.quizapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.quizapp.presentation.screens.CategoriesScreen
import com.example.quizapp.presentation.screens.QuestionView
import com.example.quizapp.presentation.screens.QuizResultView
import com.example.quizapp.presentation.viewmodel.QuizViewModel

@Composable
fun QuizNavGraph(
    navController: NavController,
    viewModel: QuizViewModel
) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = "categories"
    ) {
        composable("categories") {
            CategoriesScreen(navController = navController)
        }
        composable("quiz/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            if (viewModel.isQuizFinished.value) {
                QuizResultView(
                    viewModel = viewModel,
                    navController = navController,
                    category = category
                )
            } else {
                QuestionView(
                    viewModel = viewModel,
                    navController = navController,
                    category = category
                )
            }
        }
    }
}

