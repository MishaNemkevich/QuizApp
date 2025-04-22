package com.example.quizapp.domain.usecase

import com.example.quizapp.data.local.Question
import com.example.quizapp.domain.QuizRepository
import javax.inject.Inject

class SeedDatabaseUseCase @Inject constructor(
    private val repository: QuizRepository
) {
    suspend operator fun invoke() {
        if (repository.isEmpty()) {
            val allQuestions = listOf(
                // Python вопросы (15+)
                Question(
                    text = "Что выводит print(5 // 2) в Python?",
                    options = listOf("2", "2.5", "3", "1"),
                    correctAnswer = 0,
                    category = "Python",
                    difficulty = "Easy"
                ),
                Question(
                    text = "Какой тип данных изменяем в Python?",
                    options = listOf("Tuple", "List", "String", "Int"),
                    correctAnswer = 1,
                    category = "Python",
                    difficulty = "Easy"
                ),
                Question(
                    text = "Что делает метод .strip()?",
                    options = listOf(
                        "Удаляет пробелы с обеих сторон",
                        "Разделяет строку",
                        "Переводит в нижний регистр",
                        "Заменяет подстроку"
                    ),
                    correctAnswer = 0,
                    category = "Python",
                    difficulty = "Medium"
                ),
                Question(
                    text = "Что делает декоратор @staticmethod?",
                    options = listOf(
                        "Делает метод статическим",
                        "Делает метод приватным",
                        "Добавляет обработку исключений",
                        "Позволяет переопределять метод"
                    ),
                    correctAnswer = 0,
                    category = "Python",
                    difficulty = "Medium"
                ),
                Question(
                    text = "Как создать виртуальное окружение?",
                    options = listOf(
                        "python -m venv myenv",
                        "python create virtualenv",
                        "pip install virtualenv",
                        "python new env"
                    ),
                    correctAnswer = 0,
                    category = "Python",
                    difficulty = "Hard"
                ),

                // Kotlin вопросы (15+)
                Question(
                    text = "Как объявить переменную только для чтения в Kotlin?",
                    options = listOf("var", "val", "const", "let"),
                    correctAnswer = 1,
                    category = "Kotlin",
                    difficulty = "Easy"
                ),
                Question(
                    text = "Что такое null-safety в Kotlin?",
                    options = listOf(
                        "Гарантия отсутствия NPE",
                        "Автоматическое создание объектов",
                        "Шаблон проектирования",
                        "Тип коллекции"
                    ),
                    correctAnswer = 0,
                    category = "Kotlin",
                    difficulty = "Medium"
                ),
                Question(
                    text = "Что такое data class?",
                    options = listOf(
                        "Класс только для хранения данных",
                        "Абстрактный класс",
                        "Класс для работы с БД",
                        "Синглтон класс"
                    ),
                    correctAnswer = 0,
                    category = "Kotlin",
                    difficulty = "Easy"
                ),
                // Добавьте 10-15 вопросов для Kotlin...

                // Java вопросы (15+)
                Question(
                    text = "Что такое JVM?",
                    options = listOf(
                        "Java Virtual Machine",
                        "Java Visual Manager",
                        "JavaScript Virtual Module",
                        "Java Version Manager"
                    ),
                    correctAnswer = 0,
                    category = "Java",
                    difficulty = "Easy"
                ),
                Question(
                    text = "Какой модификатор доступа самый строгий?",
                    options = listOf("public", "protected", "default", "private"),
                    correctAnswer = 3,
                    category = "Java",
                    difficulty = "Medium"
                )
                // Добавьте 10-15 вопросов для Java...
            )
            repository.insertQuestions(allQuestions)
        }
    }
}