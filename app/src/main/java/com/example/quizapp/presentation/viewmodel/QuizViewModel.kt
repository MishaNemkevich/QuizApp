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

    fun setDifficulty(newDifficulty: String) {
        _difficulty.value = newDifficulty
    }

    fun loadQuestions(category: String) {
        _uiState.update { it.copy(
            isLoading = true,
            error = null,
            questions = emptyList(),
            currentQuestionIndex = 0,
            score = 0,
            isQuizFinished = false
        ) }

        viewModelScope.launch {
            try {
                getQuestionsUseCase(category)
                    .catch { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Unknown error occurred"
                            )
                        }
                    }
                    .collect { questions ->
                        val filteredQuestions = if (_difficulty.value != "All") {
                            questions.filter { it.difficulty == _difficulty.value }
                        } else {
                            questions
                        }.shuffled()

                        _uiState.update {
                            it.copy(
                                questions = filteredQuestions,
                                currentQuestionIndex = 0,
                                score = 0,
                                isLoading = false,
                                isQuizFinished = filteredQuestions.isEmpty()
                            )
                        }
                    }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load questions: ${e.localizedMessage}"
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
                saveResult(
                    QuizResult(
                        category = currentQuestion.category,
                        score = newScore,
                        totalQuestions = state.questions.size
                    )
                )
            }

            state.copy(
                score = newScore,
                currentQuestionIndex = if (!isFinished) nextIndex else state.currentQuestionIndex,
                isQuizFinished = isFinished,
                lastAnswerCorrect = isCorrect
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
                isQuizFinished = false,
                lastAnswerCorrect = null
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
    val lastAnswerCorrect: Boolean? = null,
    val error: String? = null
)