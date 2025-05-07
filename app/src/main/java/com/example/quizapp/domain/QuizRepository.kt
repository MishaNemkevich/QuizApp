package com.example.quizapp.domain


import com.example.quizapp.data.local.QuizResult
import com.example.quizapp.domain.model.Question
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getCategories(): Flow<List<String>>
    fun getQuestionsByCategory(category: String): Flow<List<Question>>
    suspend fun saveQuizResult(result: QuizResult)
    fun getAllResults(): Flow<List<QuizResult>>
    suspend fun isEmpty(): Boolean
    fun getQuestionsByCategoryAndDifficulty(category: String, difficulty: String?): Flow<List<Question>>
    fun getQuizHistory(): Flow<List<QuizResult>>
    fun getQuizHistoryByCategory(category: String): Flow<List<QuizResult>>
}