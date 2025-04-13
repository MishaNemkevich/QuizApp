package com.example.quizapp

import androidx.compose.foundation.layout.fillMaxSize

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.quizapp.presentation.navigation.QuizNavGraph
import com.example.quizapp.presentation.viewmodel.QuizViewModel
import com.example.quizapp.ui.theme.QuizAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val viewModel = hiltViewModel<QuizViewModel>()
                    QuizNavGraph(
                        navController = navController,
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}