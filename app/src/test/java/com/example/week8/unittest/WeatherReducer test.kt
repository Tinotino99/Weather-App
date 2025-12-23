package com.example.week8.unittest

import com.example.WeatherState
import com.example.room.WeatherEntity
import com.example.state.WeatherUiState
import com.example.week8.WeatherAction
import com.example.week8.weatherReducer
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherReducerTest {

    @Test
    fun `FetchSuccess updates weather and uiState`() {
        val oldState = WeatherState(uiState = WeatherUiState.Loading)

        val list = listOf(
            WeatherEntity(1, "Tokyo", 18.0, 55, "Clear")
        )

        val newState = weatherReducer(
            oldState,
            WeatherAction.FetchSuccess(list)
        )

        assertEquals(WeatherUiState.Success(list), newState.uiState)
        assertEquals(list, newState.weather)
    }
}
