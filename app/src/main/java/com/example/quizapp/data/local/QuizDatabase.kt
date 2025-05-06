package com.example.quizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quizapp.data.LocalDateTimeConverter

@Database(
    entities = [QuizResult::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(LocalDateTimeConverter::class)
abstract class QuizDatabase : RoomDatabase() {
    abstract fun resultsDao(): ResultsDao
}