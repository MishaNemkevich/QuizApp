package com.example.quizapp.presentation.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizapp.presentation.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    category: String,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = category) {
        viewModel.loadQuestions(category)
    }

    when {
        uiState.isLoading -> FullScreenLoader()
        uiState.error != null -> ErrorScreen(uiState.error)
        uiState.isQuizFinished -> QuizResultScreen(
            score = uiState.score,
            totalQuestions = uiState.questions.size,
            onRestart = { viewModel.resetQuiz() },
            onBackToCategories = { /* Навигация назад */ }
        )
        else -> QuestionView (
            question = uiState.questions[uiState.currentQuestionIndex],
            questionNumber = uiState.currentQuestionIndex + 1,
            totalQuestions = uiState.questions.size,
            score = uiState.score,
            onAnswerSelected = { answerIndex ->
                viewModel.checkAnswer(answerIndex)
            }
        )
    }
}