package com.example.quizapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import com.example.quizapp.R
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DifficultyDialog(
    onDismiss: () -> Unit,
    onDifficultySelected: (String) -> Unit
) {
    val difficulties = listOf("All", "Easy", "Medium", "Hard")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.select_difficulty)) },
        text = {
            Column {
                difficulties.forEach { difficulty ->
                    Button(
                        onClick = {
                            onDifficultySelected(difficulty)
                            CoroutineScope(Dispatchers.Main).launch {
                                delay(1000)
                                onDismiss()
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = when (difficulty) {
                                "All" -> stringResource(R.string.all)
                                else -> difficulty
                            }
                        )
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}