package com.example.quizapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.quizapp.data.local.Question
import androidx.lifecycle.viewModelScope
import com.example.quizapp.domain.model.QuizResult
import com.example.quizapp.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveResultUseCase: SaveResultUseCase,
    private val seedDatabaseUseCase: SeedDatabaseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            seedDatabaseUseCase() // Заполняем БД при инициализации
        }
    }

    fun loadQuestions(category: String) {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            getQuestionsUseCase(category)
                .catch { e ->
                    _uiState.update { it.copy(error = e.message, isLoading = false) }
                }
                .collect { questions ->
                    _uiState.update {
                        it.copy(
                            questions = questions,
                            currentQuestionIndex = 0,
                            score = 0,
                            isLoading = false,
                            isQuizFinished = false
                        )
                    }
                }
        }
    }

    fun checkAnswer(selectedAnswer: Int) {
        val currentState = _uiState.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex) ?: return

        val newScore = if (selectedAnswer == currentQuestion.correctAnswer) {
            currentState.score + 1
        } else {
            currentState.score
        }

        val nextQuestionIndex = currentState.currentQuestionIndex + 1
        val isFinished = nextQuestionIndex >= currentState.questions.size

        _uiState.update {
            it.copy(
                score = newScore,
                currentQuestionIndex = nextQuestionIndex,
                isQuizFinished = isFinished
            )
        }

        if (isFinished) {
            saveResult(
                QuizResult(
                    category = currentQuestion.category,
                    score = newScore,
                    totalQuestions = currentState.questions.size
                )
            )
        }
    }

    private fun saveResult(result: QuizResult) {
        viewModelScope.launch {
            saveResultUseCase(result)
        }
    }

    fun resetQuiz() {
        _uiState.update {
            it.copy(
                currentQuestionIndex = 0,
                score = 0,
                isQuizFinished = false
            )
        }
    }
}

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val isLoading: Boolean = false,
    val isQuizFinished: Boolean = false,
    val error: String? = null
)