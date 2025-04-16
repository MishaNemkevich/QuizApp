package com.example.quizapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun QuizResultScreen(
    score: Int,
    totalQuestions: Int,
    onRestart: () -> Unit,
    onBackToCategories: () -> Unit
) {
    val percentage = (score.toFloat() / totalQuestions * 100).toInt()
    val resultMessage = when {
        percentage >= 80 -> "Отличный результат! 🎉"
        percentage >= 60 -> "Хорошо! 👍"
        else -> "Попробуйте ещё раз! 💪"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = resultMessage,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "$score/$totalQuestions",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onRestart,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Попробовать снова")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackToCategories,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("К категориям")
        }
    }
}