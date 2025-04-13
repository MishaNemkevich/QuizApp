package com.example.quizapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface QuizDao {

    @Query("SELECT * FROM questions WHERE category = :category")
    suspend fun getQuestionsByCategory(category: String): List<Question>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestion(questions: List<Question> )
}