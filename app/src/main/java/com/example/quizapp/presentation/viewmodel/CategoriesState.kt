package com.example.quizapp.presentation.viewmodel

import com.example.quizapp.domain.model.Category

sealed class CategoriesState {
    object Loading : CategoriesState()
    object Empty : CategoriesState()
    data class Success(val categories: List<Category>) : CategoriesState()
    data class Error(val message: String) : CategoriesState()
}