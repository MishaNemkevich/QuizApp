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
import javax.inject.Singleton

@Singleton
class QuizRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val resultsDao: ResultsDao
) : QuizRepository {

    private val questionsCache = mutableMapOf<String, List<Question>>()

    override fun getCategories(): Flow<List<String>> = flow {
        emit(getAvailableCategories())
    }

    override fun getQuestionsByCategory(category: String): Flow<List<Question>> = flow {
        emit(loadQuestions(category))
    }

    override suspend fun saveQuizResult(result: QuizResult) {
        resultsDao.insertQuizResult(result)
    }

    override fun getAllResults(): Flow<List<QuizResult>> = resultsDao.getAllResults()

    override suspend fun isEmpty(): Boolean {
        return getAvailableCategories().all { category ->
            loadQuestions(category).isEmpty()
        }
    }

    private suspend fun loadQuestions(category: String): List<Question> {
        return questionsCache.getOrPut(category) {
            loadQuestionsFromJson(category)
        }
    }

    private suspend fun loadQuestionsFromJson(category: String): List<Question> {
        return withContext(Dispatchers.IO) {
            try {
                val resId = getResourceIdForCategory(category)
                val inputStream = context.resources.openRawResource(resId)
                val jsonString = inputStream.bufferedReader().use { it.readText() }
                val type = object : TypeToken<QuestionsData>() {}.type
                Gson().fromJson<QuestionsData>(jsonString, type).questions
            } catch (e: Exception) {
                Log.e("QuizRepository", "Error loading $category questions", e)
                emptyList()
            }
        }
    }

    override fun getQuestionsByCategoryAndDifficulty(
        category: String,
        difficulty: String?
    ): Flow<List<Question>> = flow {
        val allQuestions = loadQuestions(category)
        val filteredQuestions = when (difficulty) {
            null, "All" -> allQuestions
            else -> allQuestions.filter {
                it.difficulty.equals(difficulty, ignoreCase = true)
            }
        }
        emit(filteredQuestions)
    }

    private fun getAvailableCategories(): List<String> {
        return listOf("Python", "Kotlin", "Java")
    }

    private fun getResourceIdForCategory(category: String): Int {
        return when (category.lowercase()) {
            "python" -> R.raw.questions_python
            "kotlin" -> R.raw.questions_kotlin
            "java" -> R.raw.questions_java
            else -> throw IllegalArgumentException("Unknown category: $category")
        }
    }
}