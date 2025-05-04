package com.example.quizapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.quizapp.data.local.Question
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.QuizResult
import com.example.quizapp.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveResultUseCase: SaveResultUseCase
) : ViewModel() {

    private val _difficulty = MutableStateFlow("All")
    val difficulty: StateFlow<String> = _difficulty.asStateFlow()

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState

    fun loadQuestions(category: String, difficulty: String = "All") {
        viewModelScope.launch {
            getQuestionsUseCase(category)
                .map { questions ->
                    if (difficulty != "All") {
                        questions.filter { it.difficulty == difficulty }
                    } else {
                        questions
                    }
                }
                .collect { /* обновляем состояние */ }
        }

        viewModelScope.launch {
            try {
                getQuestionsUseCase(category)
                    .catch { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Failed to load questions"
                            )
                        }
                    }
                    .collect { questions ->
                        val filteredQuestions = when (_difficulty.value) {
                            "All" -> questions
                            else -> questions.filter { it.difficulty == _difficulty.value }
                        }.shuffled()

                        _uiState.update {
                            it.copy(
                                questions = filteredQuestions,
                                isLoading = false,
                                isQuizFinished = filteredQuestions.isEmpty(),
                                error = if (filteredQuestions.isEmpty()) "No questions available" else null
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Unexpected error: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    fun checkAnswer(selectedAnswer: Int) {
        _uiState.update { state ->
            if (state.isQuizFinished) return@update state

            val currentQuestion = state.questions.getOrNull(state.currentQuestionIndex) ?: return@update state
            val isCorrect = selectedAnswer == currentQuestion.correctAnswer
            val newScore = if (isCorrect) state.score + 1 else state.score
            val nextIndex = state.currentQuestionIndex + 1
            val isFinished = nextIndex >= state.questions.size

            if (isFinished) {
                viewModelScope.launch {
                    saveResult(
                        QuizResult(
                            category = currentQuestion.category,
                            score = newScore,
                            totalQuestions = state.questions.size,
                            difficulty = _difficulty.value,
                            date = System.currentTimeMillis()
                        )
                    )
                }
            }

            state.copy(
                score = newScore,
                currentQuestionIndex = if (!isFinished) nextIndex else state.currentQuestionIndex,
                isQuizFinished = isFinished,
                lastAnswerCorrect = isCorrect
            )
        }
    }

    private suspend fun saveResult(result: QuizResult) {
        saveResultUseCase(result)
    }

    fun resetQuiz() {
        _uiState.update { QuizUiState() }
    }
}

data class QuizUiState(
    val questions: List<Question> = emptyList(),
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val isLoading: Boolean = false,
    val isQuizFinished: Boolean = false,
    val lastAnswerCorrect: Boolean? = null,
    val error: String? = null
)
