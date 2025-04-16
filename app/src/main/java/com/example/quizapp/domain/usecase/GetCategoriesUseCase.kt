package com.example.quizapp.domain.usecase

import com.example.quizapp.R
import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.domain.model.Category
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    operator fun invoke(): Flow<List<Category>> {
        return repository.getCategories().map { categories ->
            categories.map { name ->
                Category(
                    id = name.hashCode(),
                    name = name,
                    iconResId = getIconForCategory(name)
                )
            }
        }
    }

    private fun getIconForCategory(category: String): Int {
        return when (category) {
            "Python" -> R.drawable.ic_python
            "Java" -> R.drawable.ic_java
            "Kotlin" -> R.drawable.ic_kotlin
            else -> R.drawable.ic_code
        }
    }
}