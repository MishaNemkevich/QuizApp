package com.example.quizapp.presentation.screens

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizapp.presentation.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    category: String
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadQuestions(category)
    }

    when {
        uiState.isLoading -> CircularProgressIndicator()
        uiState.error != null -> Text("Ошибка: ${uiState.error}")
        else -> {
            val currentQuestion = uiState.questions.getOrNull(uiState.currentQuestionIndex)
            if (currentQuestion != null) {
                QuestionView(
                    question = currentQuestion,
                    onAnswerSelected = { answerIndex ->
                        viewModel.checkAnswer(answerIndex)
                    }
                )
            } else {
                QuizResultView(score = uiState.score)
            }
        }
    }
}