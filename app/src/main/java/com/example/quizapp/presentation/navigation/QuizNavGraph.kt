package com.example.quizapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quizapp.presentation.screens.CategoriesScreen
import com.example.quizapp.presentation.screens.DifficultySelectionScreen
import com.example.quizapp.presentation.screens.QuizResultScreen
import com.example.quizapp.presentation.screens.QuizScreen
import com.example.quizapp.presentation.screens.SettingsScreen
import com.example.quizapp.presentation.viewmodel.QuizViewModel

@Composable
fun QuizNavHost() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.CATEGORIES
    ) {
        composable(Routes.CATEGORIES) {
            CategoriesScreen(
                onCategorySelected = { category ->
                    navController.navigate(Routes.buildQuizRoute(category))
                },
                onSettingsClick = {
                    navController.navigate(Routes.SETTINGS)
                }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.DIFFICULTY,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            DifficultySelectionScreen(
                onDifficultySelected = { difficulty ->
                    navController.navigate(Routes.buildQuizRoute(category)) {
                        popUpTo(Routes.DIFFICULTY) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.QUIZ,
            arguments = listOf(navArgument("category") { type = NavType.StringType })
        ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            val viewModel: QuizViewModel = hiltViewModel()

            LaunchedEffect(category) {
                viewModel.loadQuestions(category)
            }

            QuizScreen(
                viewModel = viewModel,
                onQuizFinished = { score, total ->
                    navController.navigate(Routes.buildResultsRoute(score, total)) {
                        popUpTo(Routes.QUIZ) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.RESULTS,
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val score = backStackEntry.arguments?.getInt("score") ?: 0
            val total = backStackEntry.arguments?.getInt("total") ?: 1

            QuizResultScreen(
                score = score,
                totalQuestions = total,
                onRestart = {
                    navController.previousBackStackEntry?.arguments?.getString("category")?.let { category ->
                        navController.navigate(Routes.buildQuizRoute(category)) {
                            popUpTo(Routes.RESULTS) { inclusive = true }
                        }
                    }
                },
                onBackToCategories = {
                    navController.popBackStack(Routes.CATEGORIES, inclusive = false)
                }
            )
        }
    }
}