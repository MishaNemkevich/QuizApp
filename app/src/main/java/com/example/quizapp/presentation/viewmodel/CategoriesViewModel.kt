package com.example.quizapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.Category
import com.example.quizapp.domain.usecase.GetCategoriesUseCase
import com.example.quizapp.domain.usecase.SeedDatabaseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val seedDatabaseUseCase: SeedDatabaseUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<CategoriesState>(CategoriesState.Loading)
    val state: StateFlow<CategoriesState> = _state

    init {
        loadCategories()
    }

    internal fun loadCategories() {
        viewModelScope.launch {
            seedDatabaseUseCase()
            getCategoriesUseCase()
                .collectLatest { categories ->
                    _state.value = if (categories.isEmpty()) {
                        CategoriesState.Empty
                    } else {
                        CategoriesState.Success(categories)
                    }
                }
        }
    }
}
