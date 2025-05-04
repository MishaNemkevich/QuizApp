package com.example.quizapp

import androidx.compose.foundation.layout.fillMaxSize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizapp.presentation.navigation.QuizNavHost
import com.example.quizapp.presentation.viewmodel.SettingsViewModel
import com.example.quizapp.ui.theme.QuizAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalizationManager.setLocale(this)

        setContent {
            QuizThemeWrapper {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuizNavHost()
                }
            }
        }
    }
}

@Composable
private fun QuizThemeWrapper(content: @Composable () -> Unit) {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val isDarkTheme = settingsViewModel.isDarkTheme.collectAsState().value

    QuizAppTheme(darkTheme = isDarkTheme) {
        content()
    }
}