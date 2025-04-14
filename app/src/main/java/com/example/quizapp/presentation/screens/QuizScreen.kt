package com.example.quizapp.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizapp.presentation.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    category: String,
    viewModel: QuizViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = category) {
        viewModel.loadQuestions(category)
    }

    when {
        uiState.isLoading -> FullScreenLoader()
        uiState.error != null -> ErrorScreen(
            error = uiState.error,
            onRetry = { viewModel.loadQuestions() } // Передаем обработчик
        )
        uiState.isQuizFinished -> QuizResultView(
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

@Composable
fun FullScreenLoader() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(
    error: String?,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = "Ошибка",
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = error ?: "Произошла ошибка",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onRetry) {
            Text("Попробовать снова")
        }
    }
}