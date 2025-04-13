package com.example.quizapp.domain

import com.example.quizapp.data.local.Question
import com.example.quizapp.domain.model.Category
import com.example.quizapp.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow

interface QuizRepository {
    suspend fun isEmpty(): Boolean
    fun getCategories(): Flow<List<Category>>
    fun getQuestionsByCategory(category: String): Flow<List<Question>>
    suspend fun insertQuestions(questions: List<Question>)
    suspend fun saveQuizResult(result: QuizResult)
    fun getAllResults(): Flow<List<QuizResult>>
}