package com.example.quizapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.data.local.Question
import com.example.quizapp.presentation.components.ErrorScreen
import com.example.quizapp.presentation.components.FullScreenLoader
import com.example.quizapp.presentation.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    category: String,
    navController: NavController,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = category) {
        viewModel.loadQuestions(category)
    }

    when {
        uiState.isLoading -> FullScreenLoader()
        uiState.error != null -> ErrorScreen(
            message = uiState.error,
            onRetry = { viewModel.loadQuestions(category) }
        )

        uiState.isQuizFinished -> QuizResultScreen(
            score = uiState.score,
            totalQuestions = uiState.questions.size,
            onRestart = { viewModel.resetQuiz() },
            onBackToCategories = { navController.popBackStack() }
        )

        else -> QuestionScreen(
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

@Composable
private fun QuestionScreen(
    question: Question,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    onAnswerSelected: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        QuestionHeader(
            questionNumber = questionNumber,
            totalQuestions = totalQuestions,
            score = score
        )

        Text(
            text = question.text,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            question.options.forEachIndexed { index, option ->
                Button(
                    onClick = { onAnswerSelected(index) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = option)
                }
            }
        }
    }
}

@Composable
private fun QuestionHeader(
    questionNumber: Int,
    totalQuestions: Int,
    score: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Вопрос $questionNumber/$totalQuestions")
        Text(text = "Счет: $score")
    }
}