package com.example.quizapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.usecase.GetCategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CategoriesState>(CategoriesState.Loading)
    val state: StateFlow<CategoriesState> = _state

    init {
        loadCategories()
    }

    fun loadCategories() {
        _state.value = CategoriesState.Loading
        viewModelScope.launch {
            getCategoriesUseCase()
                .catch { e ->
                    _state.value = CategoriesState.Error(
                        e.message ?: "Failed to load categories"
                    )
                }
                .collect { categories ->
                    _state.value = if (categories.isEmpty()) {
                        CategoriesState.Empty
                    } else {
                        CategoriesState.Success(categories)
                    }
                }
        }
    }
}