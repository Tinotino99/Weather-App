package com.example.state

import com.example.room.WeatherEntity

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val data: List<WeatherEntity>) : WeatherUiState()
    data class Error(val exception: Exception) : WeatherUiState()
    object Empty : WeatherUiState()
}

