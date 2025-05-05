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

    /**
     * Предоставляет экземпляр базы данных Room
     */
    @Provides
    @Singleton
    fun provideQuizDatabase(@ApplicationContext context: Context): QuizDatabase {
        return Room.databaseBuilder(
            context,
            QuizDatabase::class.java,
            "quiz_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    /**
     * Предоставляет реализацию QuizRepository
     */
    @Provides
    @Singleton
    fun provideQuizRepository(
        @ApplicationContext context: Context,
        database: QuizDatabase
    ): QuizRepository {
        return QuizRepositoryImpl(
            context = context,
            resultsDao = database.resultsDao()
        )
    }

    /**
     * Предоставляет маппер иконок категорий
     */
    @Provides
    @Singleton
    fun provideCategoryIconMapper(): CategoryIconMapper {
        return CategoryIconMapper()
    }

    /**
     * Предоставляет UseCase для получения категорий
     */
    @Provides
    @Singleton
    fun provideGetCategoriesUseCase(
        repository: QuizRepository,
        iconMapper: CategoryIconMapper
    ): GetCategoriesUseCase {
        return GetCategoriesUseCase(repository, iconMapper)
    }

    /**
     * Предоставляет UseCase для получения вопросов
     */
    @Provides
    @Singleton
    fun provideGetQuestionsUseCase(repository: QuizRepository): GetQuestionsUseCase {
        return GetQuestionsUseCase(repository)
    }

    /**
     * Предоставляет UseCase для сохранения результатов
     */
    @Provides
    @Singleton
    fun provideSaveResultUseCase(repository: QuizRepository): SaveResultUseCase {
        return SaveResultUseCase(repository)
    }

    /**
     * Предоставляет настройки приложения
     */
    @Provides
    @Singleton
    fun provideAppPreferences(impl: AppPreferencesImpl): AppPreferences = impl
}