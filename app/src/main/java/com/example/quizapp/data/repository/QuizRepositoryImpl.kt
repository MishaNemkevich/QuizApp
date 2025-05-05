package com.example.quizapp.data.repository

import android.content.Context
import android.util.Log
import com.example.quizapp.R
import com.example.quizapp.data.local.ResultsDao
import com.example.quizapp.data.local.QuizResult
import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.domain.model.Question
import com.example.quizapp.domain.model.QuestionsData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resultsDao: ResultsDao
) : QuizRepository {

    private var cachedQuestions: List<Question> = emptyList()

    override fun getCategories(): Flow<List<String>> = flow {
        val questions = loadQuestions()
        emit(questions.map { it.category }.distinct())
    }

    override fun getQuestionsByCategory(category: String): Flow<List<Question>> = flow {
        val questions = loadQuestions()
        emit(questions.filter { it.category == category })
    }

    override suspend fun saveQuizResult(result: QuizResult) {
        resultsDao.insertQuizResult(result)
    }

    override fun getAllResults(): Flow<List<QuizResult>> {
        return resultsDao.getAllResults()
    }

    override suspend fun isEmpty(): Boolean {
        return loadQuestions().isEmpty()
    }

    private suspend fun loadQuestions(): List<Question> {
        return if (cachedQuestions.isNotEmpty()) {
            cachedQuestions
        } else {
            loadQuestionsFromJson().also { cachedQuestions = it }
        }
    }

    // В QuizRepositoryImpl проверьте:
    private suspend fun loadQuestionsFromJson(): List<Question> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.resources.openRawResource(R.raw.questions_python)
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val type = object : TypeToken<QuestionsData>() {}.type
                val data = Gson().fromJson<QuestionsData>(jsonString, type)
                data.questions // Убедитесь, что возвращается непустой список
            } catch (e: Exception) {
                Log.e("QuizRepository", "Error loading questions", e)
                emptyList()
            }
        }
    }
}