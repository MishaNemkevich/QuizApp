package com.example.quizapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.quizapp.data.LocalDateTimeConverter
import java.time.LocalDateTime

/**
 * Результат прохождения квиза
 * @property id Уникальный идентификатор
 * @property category Категория квиза
 * @property score Количество правильных ответов
 * @property totalQuestions Всего вопросов
 * @property difficulty Выбранная сложность
 * @property date Дата прохождения (timestamp в миллисекундах)
 */
@Entity(tableName = "quiz_results")
@TypeConverters(LocalDateTimeConverter::class)
data class QuizResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: String,
    val score: Int,
    val totalQuestions: Int,
    val difficulty: String,
    val date: LocalDateTime // Изменено с Long на LocalDateTime
)