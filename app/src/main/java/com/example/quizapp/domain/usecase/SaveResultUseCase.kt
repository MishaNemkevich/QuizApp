package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.domain.model.QuizResult
import javax.inject.Inject

class SaveResultUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    suspend operator fun invoke(result: QuizResult) {
        require(result.score >= 0) { "Score cannot be negative" }
        require(result.totalQuestions > 0) { "Total questions must be positive" }
        repository.saveQuizResult(result)
    }
}