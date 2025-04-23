package com.example.quizapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferences: AppPreferences
) : ViewModel() {
    val isDarkTheme: StateFlow<Boolean> = preferences.isDarkTheme

    fun toggleTheme() {
        viewModelScope.launch {
            preferences.setDarkTheme(!isDarkTheme.value)
        }
    }
}