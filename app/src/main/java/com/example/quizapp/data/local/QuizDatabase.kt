package com.example.quizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizapp.data.Converters
import com.example.quizapp.data.LocalDateTimeConverter

@Database(
    entities = [Question::class, QuizResult::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class, LocalDateTimeConverter::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun quizDao(): QuizDao
}