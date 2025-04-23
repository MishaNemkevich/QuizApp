package com.example.quizapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.quizapp.data.local.Question
import com.example.quizapp.presentation.components.EmptyQuizScreen
import com.example.quizapp.presentation.components.ErrorScreen
import com.example.quizapp.presentation.components.FullScreenLoader
import com.example.quizapp.presentation.viewmodel.QuizViewModel
import androidx.compose.runtime.*
import com.example.quizapp.R
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource

@Composable
fun QuizScreen(
    viewModel: QuizViewModel = hiltViewModel(),
    onQuizFinished: (Int, Int) -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.isQuizFinished) {
        if (uiState.isQuizFinished && uiState.questions.isNotEmpty()) {
            onQuizFinished(uiState.score, uiState.questions.size)
        }
    }

    when {
        uiState.isLoading -> FullScreenLoader(
            text = stringResource(R.string.loading)
        )
        uiState.error != null -> ErrorScreen(
            message = uiState.error ?: stringResource(R.string.error_occurred),
            onRetry = { viewModel.resetQuiz() },
            onBack = onBack
        )
        uiState.questions.isEmpty() -> EmptyQuizScreen(
            message = stringResource(R.string.no_questions),
            onBack = onBack
        )
        else -> {
            val currentQuestion = uiState.questions[uiState.currentQuestionIndex]
            QuestionScreen(
                question = currentQuestion,
                questionNumber = uiState.currentQuestionIndex + 1,
                totalQuestions = uiState.questions.size,
                score = uiState.score,
                onAnswerSelected = viewModel::checkAnswer,
                onBack = onBack
            )
        }
    }
}

@Composable
private fun QuestionScreen(
    question: Question,
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    onAnswerSelected: (Int) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        QuestionHeader(
            questionNumber = questionNumber,
            totalQuestions = totalQuestions,
            score = score,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = question.text,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        AnswersGrid(
            options = question.options,
            onAnswerSelected = onAnswerSelected,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Composable
private fun AnswerButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(vertical = 12.dp)
                .fillMaxWidth()
        )
    }
}
@Composable
private fun AnswersGrid(
    options: List<String>,
    onAnswerSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(options) { index, option ->
            AnswerButton(
                text = option,
                onClick = { onAnswerSelected(index) }
            )
        }
    }
}

@Composable
private fun QuestionHeader(
    questionNumber: Int,
    totalQuestions: Int,
    score: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Вопрос $questionNumber/$totalQuestions",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Badge(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Text(text = "Очки: $score")
        }
    }
}
