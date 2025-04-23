package com.example.quizapp.presentation.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizapp.LocalizationManager
import com.example.quizapp.R
import com.example.quizapp.presentation.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activity = context.findActivity()

    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.dark_theme),
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = viewModel.isDarkTheme.collectAsState().value,
                onCheckedChange = { viewModel.toggleTheme() }
            )
        }

        Text(text = stringResource(R.string.language_settings))
        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = {
                    LocalizationManager.setNewLocale(context, "ru")
                    activity?.recreate()
                }
            ) {
                Text("Русский")
            }

            Button(
                onClick = {
                    LocalizationManager.setNewLocale(context, "en")
                    activity?.recreate()
                }
            ) {
                Text("English")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Button(onClick = onBack) {
            Text(stringResource(R.string.back))
        }
    }
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}