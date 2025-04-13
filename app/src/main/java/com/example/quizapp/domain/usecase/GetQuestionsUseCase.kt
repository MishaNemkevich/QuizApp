package com.example.quizapp.domain.usecase

import com.example.quizapp.data.local.Question
import com.example.quizapp.data.repository.QuizRepository
import javax.inject.Inject

class GetQuestionsUseCase @Inject constructor(
    private val repository: QuizRepository
){

    suspend operator fun invoke(category: String): List<Question>{
        return repository.getQuestions(category)
    }
}