package com.example.quizapp.domain.usecase

import android.util.Log
import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.domain.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    operator fun invoke(category: String, difficulty: String = "All"): Flow<List<Question>> {
        return repository.getQuestionsByCategory(category)
            .map { questions ->
                when (difficulty) {
                    "All" -> questions
                    else -> questions.filter { q ->
                        q.difficulty.equals(difficulty, ignoreCase = false)  // Регистр важен!
                    }.also { filtered ->
                        if (filtered.isEmpty()) {
                            Log.w("FILTER", "No $difficulty questions in $category")
                        }
                    }
                }
            }
    }
}