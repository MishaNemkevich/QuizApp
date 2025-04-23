package com.example.quizapp.presentation.navigation

object Routes {
    const val CATEGORIES = "categories"
    const val QUIZ = "quiz/{category}"
    const val RESULTS = "results/{score}/{total}"
    const val DIFFICULTY = "difficulty/{category}"
    const val SETTINGS = "setting"

    fun buildQuizRoute(category: String) = "quiz/$category"
    fun buildResultsRoute(score: Int, total: Int) = "results/$score/$total"
    fun buildDifficultyRoute(category: String) = "difficulty/$category"
}