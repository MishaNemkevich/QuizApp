package com.example.quizapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizapp.R
import com.example.quizapp.presentation.viewmodel.QuizViewModel

@Composable
fun QuizResultScreen(
    score: Int,
    totalQuestions: Int,
    viewModel: QuizViewModel = hiltViewModel(),
    onBackToCategories: () -> Unit
) {
    val percentage = (score.toFloat() / totalQuestions * 100).toInt()
    val resultMessage = when {
        percentage >= 80 -> stringResource(R.string.result_excellent)
        percentage >= 60 -> stringResource(R.string.result_good)
        else -> stringResource(R.string.result_try_again)
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
            onClick = { viewModel.restartQuiz() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.restart))
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackToCategories,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.back_to_categories))
        }
    }
}