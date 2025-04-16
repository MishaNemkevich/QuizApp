package com.example.quizapp.domain

import com.example.quizapp.data.local.Question
import com.example.quizapp.domain.model.Category
import com.example.quizapp.data.local.QuizResult
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    fun getCategories(): Flow<List<String>>
    fun getQuestionsByCategory(category: String): Flow<List<Question>>
    suspend fun insertQuestions(questions: List<Question>)
    suspend fun saveQuizResult(result: QuizResult)
    fun getAllResults(): Flow<List<QuizResult>>
    suspend fun isEmpty(): Boolean
}