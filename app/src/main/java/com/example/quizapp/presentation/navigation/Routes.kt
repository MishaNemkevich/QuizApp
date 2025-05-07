package com.example.quizapp.presentation.navigation

object Routes {
    const val CATEGORIES = "categories"
    const val SETTINGS = "settings"
    const val QUIZ = "quiz/{category}/{difficulty}"
    const val RESULTS = "results/{score}/{total}"
    const val HISTORY = "history"

    fun buildQuizRoute(category: String, difficulty: String = "All"): String {
        return "quiz/$category/$difficulty"
    }

    fun buildResultsRoute(score: Int, total: Int): String {
        return "results/$score/$total"
    }
}