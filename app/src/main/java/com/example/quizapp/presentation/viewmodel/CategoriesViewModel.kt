package com.example.quizapp.presentation.viewmodel

import android.util.Log
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.Category
import com.example.quizapp.domain.usecase.GetCategoriesUseCase
import com.example.quizapp.domain.usecase.SeedDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val seedDatabaseUseCase: SeedDatabaseUseCase
) : ViewModel() {
    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {

            seedDatabaseUseCase()

            getCategoriesUseCase()
                .catch { e ->

                    Log.e("CategoriesViewModel", "Error loading categories", e)
                }
                .collect { categories ->
                    _categories.value = categories.map {
                        Category(
                            name = it.toString(),
                            icon = getIconForCategory(it.toString())
                        )
                    }
                    _isLoading.value = false
                }
        }
    }

    private fun getIconForCategory(category: String): ImageVector {
        return when (category) {
            "Python" -> Icons.Default.Code
            "Java" -> Icons.Default.Computer
            "Kotlin" -> Icons.Default.Android
            else -> Icons.Default.QuestionMark
        }
    }
}