package com.example.quizapp.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.quizapp.presentation.viewmodel.CategoriesViewModel

@Composable
fun CategoriesScreen(
    navController: NavController,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val categories by viewModel.categories.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    if (isLoading) {
        FullScreenLoader()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Выберите категорию",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(
                        category = category,
                        onClick = { navController.navigate("quiz/${category.name}") }
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryCard(
    category: Int,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}