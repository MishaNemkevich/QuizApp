package com.example.quizapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.quizapp.data.local.QuizResult
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("SELECT DISTINCT category FROM questions")
    fun getCategories(): Flow<List<String>>

    @Query("SELECT * FROM questions WHERE category = :category")
    fun getQuestionsByCategory(category: String): Flow<List<Question>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuestions(questions: List<Question>)

    @Insert
    suspend fun insertQuizResult(result: QuizResult)

    @Query("SELECT * FROM quiz_results ORDER BY date DESC")
    fun getAllResults(): Flow<List<QuizResult>>

    @Query("SELECT COUNT(*) FROM questions")
    suspend fun getQuestionsCount(): Int
}