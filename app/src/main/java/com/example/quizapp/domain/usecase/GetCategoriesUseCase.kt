package com.example.quizapp.domain.usecase

import com.example.quizapp.R
import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: QuizRepository,
    private val categoryIconMapper: CategoryIconMapper
) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getCategories()
            .map { categoryNames ->
                categoryNames.map { name ->
                    Category(
                        name = name,
                        iconResId = categoryIconMapper.getIconResId(name)
                    )
                }
            }
    }
}

class CategoryIconMapper @Inject constructor() {
    fun getIconResId(categoryName: String): Int {
        return when (categoryName) {
            "Python" -> R.drawable.ic_python
            "Java" -> R.drawable.ic_java
            "Kotlin" -> R.drawable.ic_kotlin
            else -> R.drawable.ic_code
        }
    }
}