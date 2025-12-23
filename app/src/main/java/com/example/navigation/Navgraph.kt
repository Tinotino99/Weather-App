package com.example.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.DetailsScreen
import com.example.viewmodel.WeatherViewModel

@Composable
fun NavGraph(viewModel: WeatherViewModel) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        composable("home") {
            HomeScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("details") {
            DetailsScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}
