package com.example.quizapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "quiz_results")
data class QuizResult(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val category: String,
    val score: Int,
    val totalQuestions: Int,
    val date: LocalDateTime = LocalDateTime.now()
)