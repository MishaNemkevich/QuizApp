package com.example.quizapp.domain.usecase

import com.example.quizapp.data.local.Question
import com.example.quizapp.domain.QuizRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    operator fun invoke(category: String): Flow<List<Question>> {
        return repository.getQuestionsByCategory(category)
    }
}