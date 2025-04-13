package com.example.quizapp.presentation.screens

import android.annotation.SuppressLint
import com.example.quizapp.presentation.viewmodel.QuizViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun QuizResultView(
    viewModel: QuizViewModel,
    navController: NavController,
    category: String
) {
    val score = viewModel.score.value
    val totalQuestions = viewModel.questions.value.size
    val percentage = (score.toFloat() / totalQuestions.toFloat() * 100).toInt()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Результат",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "$score/$totalQuestions",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Text(
            text = "$percentage%",
            fontSize = 24.sp,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = when {
                percentage >= 80 -> "Отлично! 🎉"
                percentage >= 60 -> "Хорошо! 👍"
                else -> "Попробуйте еще раз! 💪"
            },
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                viewModel.resetQuiz()
                navController.popBackStack()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "К категориям")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.resetQuiz()
                navController.navigate("quiz/$category")
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Попробовать снова")
        }
    }
}