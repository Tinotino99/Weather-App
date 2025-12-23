package com.example.week8

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.network.service
import com.example.navigation.NavGraph
import com.example.repository.WeatherRepository
import com.example.viewmodel.WeatherViewModel
import com.example.viewmodel.WeatherViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val context = applicationContext

        val viewModel: WeatherViewModel by viewModels {
            WeatherViewModelFactory(
                repo = WeatherRepository(
                    api = service,
                    context = context
                )
            )
        }

        setContent {
            NavGraph(viewModel = viewModel)
        }
    }
}