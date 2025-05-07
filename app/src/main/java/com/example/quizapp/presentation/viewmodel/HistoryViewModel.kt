package com.example.quizapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.QuizResult
import com.example.quizapp.domain.QuizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val repository: QuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun loadHistory(category: String? = null) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val historyFlow = if (category != null) {
                    repository.getQuizHistoryByCategory(category)
                } else {
                    repository.getQuizHistory()
                }

                historyFlow.collect { results ->
                    _uiState.update {
                        it.copy(
                            results = results.sortedByDescending { it.date },
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = e.message ?: "Failed to load history",
                        isLoading = false
                    )
                }
            }
        }
    }
}

data class HistoryUiState(
    val results: List<QuizResult> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)