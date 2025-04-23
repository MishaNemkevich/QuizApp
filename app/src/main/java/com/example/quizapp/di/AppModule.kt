package com.example.quizapp.di

import android.content.Context
import androidx.room.Room
import com.example.quizapp.AppPreferences
import com.example.quizapp.AppPreferencesImpl
import com.example.quizapp.data.local.QuizDatabase
import com.example.quizapp.data.repository.QuizRepositoryImpl
import com.example.quizapp.domain.QuizRepository
import com.example.quizapp.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideQuizDatabase(@ApplicationContext context: Context): QuizDatabase {
        return Room.databaseBuilder(
            context,
            QuizDatabase::class.java,
            "quiz_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideQuizRepository(database: QuizDatabase): QuizRepository {
        return QuizRepositoryImpl(database.quizDao())
    }

    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(repository: QuizRepository): GetCategoriesUseCase {
        return GetCategoriesUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideAppPreferences(impl: AppPreferencesImpl): AppPreferences = impl

    @Provides
    @Singleton
    fun provideGetQuestionsUseCase(repository: QuizRepository): GetQuestionsUseCase {
        return GetQuestionsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSaveResultUseCase(repository: QuizRepository): SaveResultUseCase {
        return SaveResultUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSeedDatabaseUseCase(repository: QuizRepository): SeedDatabaseUseCase {
        return SeedDatabaseUseCase(repository)
    }
}