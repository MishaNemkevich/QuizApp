package com.example.quizapp.data.repository

import com.example.quizapp.data.local.Question
import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.data.local.QuizDao
import com.example.quizapp.data.local.QuizResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    private val quizDao: QuizDao
) : QuizRepository {

    override fun getCategories(): Flow<List<String>> = quizDao.getCategories()

    override fun getQuestionsByCategory(category: String): Flow<List<Question>> =
        quizDao.getQuestionsByCategory(category)

    override suspend fun insertQuestions(questions: List<Question>) =
        quizDao.insertQuestions(questions)

    override suspend fun saveQuizResult(result: QuizResult) =
        quizDao.insertQuizResult(result)

    override fun getAllResults(): Flow<List<QuizResult>> =
        quizDao.getAllResults()

    override suspend fun isEmpty(): Boolean =
        quizDao.getQuestionsCount() == 0
}