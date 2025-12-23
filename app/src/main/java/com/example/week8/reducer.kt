package com.example.week8

import com.example.WeatherState
import com.example.state.WeatherUiState

fun weatherReducer(oldState: WeatherState, action: WeatherAction): WeatherState {
    return when (action) {


        is WeatherAction.FetchError -> {
            val exception = Exception(action.error)
            oldState.copy(
                uiState = WeatherUiState.Error(exception),
                error = exception
            )
        }

        is WeatherAction.FetchListOfWeather -> {
            oldState.copy(
                uiState = WeatherUiState.Loading,
                error = null
            )
        }

        is WeatherAction.FetchSuccess -> {
            oldState.copy(
                uiState = WeatherUiState.Success(action.weather),
                weather = action.weather,
                error = null
            )
        }

        is WeatherAction.SelectWeather -> {
            oldState.copy(
                selectedWeather = action.weather
            )
        }
    }
}
