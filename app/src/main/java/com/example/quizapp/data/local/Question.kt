package com.example.quizapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "questions")
data class Question(
    @PrimaryKey val id: Int,
    val text: String,
    val options: List<String>,
    val correctAnswer: Int,
    val category: String,
    val difficulty: String
)