package com.example.quizapp.domain.usecase

import android.util.Log
import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.data.local.QuizResult
import javax.inject.Inject

class SaveResultUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    private companion object {
        const val TAG = "SaveResultUseCase"
        const val MIN_SCORE = 0
    }

    suspend operator fun invoke(result: QuizResult) {
        try {
            validateResult(result)
            Log.d(TAG, "Saving result for ${result.category}: " +
                    "${result.score}/${result.totalQuestions}")

            repository.saveQuizResult(result)

            Log.d(TAG, "Result saved successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error saving quiz result", e)
            throw e // или обработать ошибку специфическим образом
        }
    }

    private fun validateResult(result: QuizResult) {
        require(result.score in MIN_SCORE..result.totalQuestions) {
            "Invalid score: ${result.score} (must be 0..${result.totalQuestions})"
        }
        require(result.category.isNotBlank()) {
            "Category cannot be blank"
        }
        require(result.totalQuestions > 0) {
            "Total questions must be positive"
        }
    }
}