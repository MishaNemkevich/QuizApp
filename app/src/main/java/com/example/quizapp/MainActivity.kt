package com.example.quizapp

import android.os.Build
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
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.ExistingPeriodicWorkPolicy
import java.util.concurrent.TimeUnit
import com.example.quizapp.domain.QuizReminderWorker
import com.example.quizapp.presentation.navigation.QuizNavHost
import com.example.quizapp.presentation.viewmodel.SettingsViewModel
import com.example.quizapp.ui.theme.QuizAppTheme
import dagger.hilt.android.AndroidEntryPoint
import android.Manifest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalizationManager.setLocale(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                100
            )
        }

        setupDailyReminder()

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
    private fun setupDailyReminder() {
        val workRequest = PeriodicWorkRequestBuilder<QuizReminderWorker>(
            1, TimeUnit.MINUTES // Повторять каждые 24 часа
        ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "quiz_reminder",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
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