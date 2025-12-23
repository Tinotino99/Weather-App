package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.viewmodel.WeatherViewModel
import com.example.state.WeatherUiState
import com.example.room.WeatherEntity

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    viewModel: WeatherViewModel,
    navController: NavController
) {
    val state = viewModel.state
    val uiState = state.uiState

    LaunchedEffect(Unit) {
        viewModel.refreshHomePage()
    }

    val isLoading = uiState is WeatherUiState.Loading

    val refreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { viewModel.refreshHomePage() }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .pullRefresh(refreshState)
    ) {

        when (uiState) {

            is WeatherUiState.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Loading...")
                }
            }

            is WeatherUiState.Success -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(uiState.data) { weather: WeatherEntity ->

                        Text(
                            text = weather.city,
                            modifier = Modifier
                                .clickable {
                                    viewModel.selectWeather(weather)
                                    navController.navigate("details")
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }

            is WeatherUiState.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Error: ${uiState.exception.message}")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.retry() }) {
                        Text("Retry")
                    }
                }
            }

            WeatherUiState.Empty -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No weather data available.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { viewModel.refreshHomePage() }) {
                        Text("Refresh")
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isLoading,
            state = refreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
}
