package com.example

import com.example.room.WeatherEntity
import com.example.state.WeatherUiState


data class WeatherState (
    val selectedWeather : WeatherEntity? = null,
    val error: Exception? = null,
    val uiState: WeatherUiState = WeatherUiState.Loading,
    val weather: List<WeatherEntity>? = null
    )