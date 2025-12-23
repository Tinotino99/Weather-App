package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    navController: NavController,
    viewModel: WeatherViewModel
) {
    val state = viewModel.state
    val selected = state.selectedWeather

    LaunchedEffect(Unit) {
        viewModel.refreshDetailsPage()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(selected?.city ?: "Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            if (selected != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {

                        Text(
                            text = selected.city,
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Text("🌡 Temperature: ${selected.temp}°C")
                        Text("💧 Humidity: ${selected.humidity}%")
                        Text("☁ Condition: ${selected.condition}")
                    }
                }
            } else {
                Text("No weather selected")
            }
        }
    }
}
