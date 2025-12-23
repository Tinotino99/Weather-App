package com.example.week8

import com.example.room.WeatherEntity

sealed class WeatherAction {

    object FetchListOfWeather : WeatherAction()

    data class FetchSuccess(val weather: List<WeatherEntity>) : WeatherAction()

    data class FetchError(val error: String) : WeatherAction()

    data class SelectWeather(val weather: WeatherEntity) : WeatherAction()
}
