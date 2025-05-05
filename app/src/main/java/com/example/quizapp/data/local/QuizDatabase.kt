package com.example.quizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizapp.data.LocalDateTimeConverter

@Database(
    entities = [QuizResult::class], // Только результаты
    version = 3, // Сбрасываем версию
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class) // Только конвертер даты
abstract class QuizDatabase : RoomDatabase() {
    abstract fun resultsDao(): ResultsDao // Переименованный DAO
}