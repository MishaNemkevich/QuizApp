package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetResultUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    operator fun invoke(): Flow<List<QuizResult>> {
        return repository.getAllResults()
    }
}