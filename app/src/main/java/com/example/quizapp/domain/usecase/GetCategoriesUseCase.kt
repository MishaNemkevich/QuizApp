package com.example.quizapp.domain.usecase

import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.domain.model.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getCategories()
    }
}