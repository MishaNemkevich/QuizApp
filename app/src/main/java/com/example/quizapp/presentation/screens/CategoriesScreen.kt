package com.example.quizapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.quizapp.domain.model.Category
import com.example.quizapp.presentation.components.EmptyCategoriesScreen
import com.example.quizapp.presentation.components.ErrorScreen
import com.example.quizapp.presentation.components.FullScreenLoader
import com.example.quizapp.presentation.viewmodel.CategoriesState
import com.example.quizapp.presentation.viewmodel.CategoriesViewModel

@Composable
fun CategoriesScreen(
    onStartQuiz: (category: String, difficulty: String) -> Unit,
    onSettingsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value
    var showDifficultyDialog = remember { mutableStateOf(false) }
    var selectedCategory = remember { mutableStateOf("") }

    if (showDifficultyDialog.value) {
        DifficultyDialog(
            onDifficultySelected = { difficulty ->
                onStartQuiz(selectedCategory.value, difficulty)
                showDifficultyDialog.value = false
            },
            onDismiss = { showDifficultyDialog.value = false }
        )
    }

    LaunchedEffect(Unit) {
        viewModel.loadCategories()
    }

    Scaffold(
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingActionButton(
                    onClick = onHistoryClick,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.History,
                        contentDescription = "История"
                    )
                }
                FloatingActionButton(
                    onClick = onSettingsClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Настройки"
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (val currentState = state) {
                is CategoriesState.Loading -> FullScreenLoader()
                is CategoriesState.Empty -> EmptyCategoriesScreen()
                is CategoriesState.Error -> ErrorScreen(
                    message = currentState.message,
                    onRetry = { viewModel.loadCategories() },
                    onBack = { /* Обработка назад */ }
                )
                is CategoriesState.Success -> CategoryGrid(
                    categories = currentState.categories,
                    onCategoryClick = { category ->
                        selectedCategory.value = category.name
                        showDifficultyDialog.value = true
                    }
                )
            }
        }
    }
}

@Composable
private fun CategoryGrid(
    categories: List<Category>,
    onCategoryClick: (Category) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories) { category ->
            CategoryCard(
                category = category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@Composable
private fun CategoryCard(
    category: Category,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = category.iconResId),
                contentDescription = category.name,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = category.name)
        }
    }
}