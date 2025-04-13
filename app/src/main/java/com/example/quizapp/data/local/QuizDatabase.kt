package com.example.quizapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quizapp.data.local.Question

@Database(entities = [Question::class],
    version = 1,
    exportSchema = false )
abstract class QuizDatabase: RoomDatabase() {

    abstract fun quizDao():  QuizDao

    companion object {
        const val DATABASE_NAME = "quiz_db"
    }


}