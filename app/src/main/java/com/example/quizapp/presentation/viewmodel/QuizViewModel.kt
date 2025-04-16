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

    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState

    fun loadQuestions(category: String) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getQuestionsUseCase(category)
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
        _uiState.update { state ->
            val currentQuestion = state.questions.getOrNull(state.currentQuestionIndex) ?: return
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
                currentQuestionIndex = nextIndex,
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