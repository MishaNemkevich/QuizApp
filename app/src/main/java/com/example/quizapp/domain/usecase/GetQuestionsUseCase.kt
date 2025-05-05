package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.domain.model.Question
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    private companion object {
        const val TAG = "GetQuestionsUseCase"
    }

    operator fun invoke(category: String, difficulty: String? = null): Flow<List<Question>> {
        return repository.getQuestionsByCategory(category)
            .map { questions ->
                difficulty?.takeIf { it != "All" }?.let { diff ->
                    questions.filter {
                        it.difficulty.equals(diff, ignoreCase = true)
                    }
                } ?: questions
            }
    }
}