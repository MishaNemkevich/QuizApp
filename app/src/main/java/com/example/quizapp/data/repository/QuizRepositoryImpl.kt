package com.example.quizapp.data.repository

import com.example.quizapp.data.local.Question
import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.data.local.QuizDao
import com.example.quizapp.domain.model.Category
import com.example.quizapp.domain.model.QuizResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val quizDao: QuizDao
) : QuizRepository {

    override suspend fun isEmpty(): Boolean {
        return quizDao.getQuestionsCount() == 0
    }

    override fun getCategories(): Flow<List<Category>> {
        return quizDao.getCategories()
    }
    override fun getQuestionsByCategory(category: String): Flow<List<Question>> {
        return quizDao.getQuestionsByCategory(category)
    }

    override suspend fun insertQuestions(questions: List<Question>) {
        quizDao.insertQuestions(questions)
    }

    override suspend fun saveQuizResult(result: QuizResult) {
        quizDao.insertQuizResult(result)
    }

    override fun getAllResults(): Flow<List<QuizResult>> {
        return quizDao.getAllResults()
    }
}