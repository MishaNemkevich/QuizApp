package com.example.quizapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.quizapp.data.local.Question

@Composable
fun QuestionView(
    question: Question,
    onAnswerSelected: (Int) -> Unit
) {
    Column {
        Text(question.text)
        question.options.forEachIndexed { index, option ->
            Button(onClick = { onAnswerSelected(index) }) {
                Text(option)
            }
        }
    }
}