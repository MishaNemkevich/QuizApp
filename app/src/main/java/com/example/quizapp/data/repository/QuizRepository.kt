package com.example.quizapp.data.repository

import com.example.quizapp.data.local.Question
import com.example.quizapp.data.local.QuizDao
import javax.inject.Inject

class QuizRepository @Inject constructor(
    private val quizDao: QuizDao
) {
    suspend fun getQuestions(category: String): List<Question> {
        return quizDao.getQuestionsByCategory(category)
    }
}