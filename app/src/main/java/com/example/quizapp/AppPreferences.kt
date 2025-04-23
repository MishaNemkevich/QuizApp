package com.example.quizapp

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import javax.inject.Singleton

interface AppPreferences {
    val isDarkTheme: StateFlow<Boolean>
    suspend fun setDarkTheme(enabled: Boolean)
}

@Singleton
class AppPreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AppPreferences {

    private val Context.dataStore by preferencesDataStore(name = "settings")

    private object PreferencesKeys {
        val DARK_THEME = booleanPreferencesKey("dark_theme")
    }

    override val isDarkTheme: StateFlow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[PreferencesKeys.DARK_THEME] ?: false
        }
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    override suspend fun setDarkTheme(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DARK_THEME] = enabled
        }
    }
}