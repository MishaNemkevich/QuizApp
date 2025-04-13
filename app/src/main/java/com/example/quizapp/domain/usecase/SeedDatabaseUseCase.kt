package com.example.quizapp.domain.usecase

import com.example.quizapp.data.local.Question
import com.example.quizapp.domain.QuizRepository
import javax.inject.Inject

class SeedDatabaseUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    suspend operator fun invoke() {
        if (repository.isEmpty()) {
            val defaultQuestions = listOf(
                Question(
                    id =1,
                    text = "Что выводит print(5 // 2) в Python?",
                    options = listOf("2", "2.5", "3", "1"),
                    correctAnswer = 0,
                    category = "Python",
                    difficulty = "Easy"
                ),
                Question(
                    id = 2,
                    text = "Как объявить переменную в Kotlin?",
                    options = listOf("val x = 5", "var x = 5", "let x = 5", "const x = 5"),
                    correctAnswer = 1,
                    category = "Kotlin",
                    difficulty = "Easy"
                )
            )
            repository.insertQuestions(defaultQuestions)
        }
    }
}