package com.example.quizapp.domain.model

data class QuestionsData(
    val version: Int = 1,
    val questions: List<Question>
)

/**
 * Модель вопроса викторины
 * @property text Текст вопроса
 * @property options Список вариантов ответа (минимум 2)
 * @property correctAnswer Индекс правильного ответа (начиная с 0)
 * @property category Категория (должна соответствовать Category.name)
 * @property difficulty Уровень сложности (Easy/Medium/Hard)
 */
data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: Int,
    val category: String,
    val difficulty: String
) {
    init {
        require(options.size >= 2) { "Question must have at least 2 options" }
        require(correctAnswer in options.indices) {
            "Correct answer index out of bounds"
        }
    }
}