package com.example.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.WeatherState
import com.example.repository.WeatherRepository
import com.example.week8.WeatherAction
import com.example.week8.weatherReducer
import com.example.room.WeatherEntity
import kotlinx.coroutines.launch

class WeatherViewModel(private val repo: WeatherRepository) : ViewModel() {

    private val _state = mutableStateOf(WeatherState())
    val state: WeatherState get() = _state.value

    val uiState: State<WeatherState> = _state

    private fun fireAction(action: WeatherAction) {
        _state.value = weatherReducer(
            oldState = _state.value,
            action = action
        )
    }

    fun refreshHomePage() {

        fireAction(WeatherAction.FetchListOfWeather)

        viewModelScope.launch {
            try {
                val list = repo.getWeatherList()

                fireAction(WeatherAction.FetchSuccess(list))

            } catch (e: Exception) {

                fireAction(
                    WeatherAction.FetchError(
                        e.message ?: "Unknown error"
                    )
                )
            }
        }
    }

    fun refreshDetailsPage() {
        viewModelScope.launch {

            val id = _state.value.selectedWeather?.id ?: return@launch

            val updated = repo.getWeatherById(id)

            if (updated != null) {
                fireAction(
                    WeatherAction.SelectWeather(updated)
                )
            }
        }
    }

    fun selectWeather(item: WeatherEntity) {
        fireAction(WeatherAction.SelectWeather(item))
    }

    fun retry() = refreshHomePage()
}
