package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.data.local.QuizResult
import javax.inject.Inject

class SaveResultUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    suspend operator fun invoke(result: QuizResult) {
        repository.saveQuizResult(result)
    }
}