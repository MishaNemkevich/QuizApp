package com.example.quizapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.local.QuizResult
import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.usecase.GetQuestionsUseCase
import com.example.quizapp.domain.usecase.SaveResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val getQuestionsUseCase: GetQuestionsUseCase,
    private val saveResultUseCase: SaveResultUseCase
) : ViewModel() {

    private val _difficulty = MutableStateFlow("All")
    val difficulty: StateFlow<String> = _difficulty.asStateFlow()

    private val _category = MutableStateFlow("")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState

    fun loadQuestions(category: String) {
        _category.value = category
        _uiState.update {
            it.copy(
                isLoading = true,
                error = null,
                isQuizFinished = false,
                currentQuestionIndex = 0,
                score = 0
            )
        }

        viewModelScope.launch {
            getQuestionsUseCase(category, _difficulty.value)
                .catch { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Failed to load questions"
                        )
                    }
                }
                .collect { questions ->
                    _uiState.update {
                        it.copy(
                            questions = questions.shuffled(),
                            isLoading = false,
                            isQuizFinished = questions.isEmpty(),
                            error = if (questions.isEmpty()) "No questions available" else null
                        )
                    }
                }
        }
    }

    fun checkAnswer(selectedAnswer: Int) {
        _uiState.update { state ->
            if (state.isQuizFinished || state.questions.isEmpty()) return@update state

            val currentQuestion = state.questions[state.currentQuestionIndex]
            val isCorrect = selectedAnswer == currentQuestion.correctAnswer
            val newScore = if (isCorrect) state.score + 1 else state.score
            val nextIndex = state.currentQuestionIndex + 1
            val isFinished = nextIndex >= state.questions.size

            if (isFinished) {
                saveQuizResult(
                    category = currentQuestion.category,
                    score = newScore,
                    total = state.questions.size
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

    private fun saveQuizResult(category: String, score: Int, total: Int) {
        viewModelScope.launch {
            saveResultUseCase(
                QuizResult(
                    category = category,
                    score = score,
                    totalQuestions = total,
                    difficulty = _difficulty.value,
                    date = LocalDateTime.now()
                )
            )
        }
    }

    fun changeDifficulty(difficulty: String) {
        _difficulty.value = difficulty
        if (_category.value.isNotEmpty()) {
            loadQuestions(_category.value)
        }
    }

    fun resetQuiz() {
        _uiState.update {
            QuizUiState(
                questions = emptyList(),
                currentQuestionIndex = 0,
                score = 0,
                isLoading = false,
                isQuizFinished = false,
                lastAnswerCorrect = null,
                error = null
            )
        }
    }
    fun restartQuiz() {
        resetQuiz()
        if (_category.value.isNotEmpty()) {
            loadQuestions(_category.value)
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