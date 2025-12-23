package com.example.repository

import android.content.Context
import androidx.room.Room
import com.example.network.ApiService
import com.example.room.AppDatabase
import com.example.room.WeatherEntity
import java.lang.Exception

class WeatherRepository(
    private val api: ApiService,
    context: Context
) {
    private val db = Room.databaseBuilder(
        context,
        AppDatabase::class.java,
        "weather-db"
    ).build()
    private val dao = db.weatherDao()

    suspend fun getWeatherList(): List<WeatherEntity> {
        val cached = dao.getAll()
        if (cached.isNotEmpty()) return cached

        val cities = listOf("London", "New York", "Tokyo", "Paris")
        val apiList = cities.mapNotNull { city ->
            try {
                val w = api.getWeatherByCity(city, "47581f4a34583a34272c732b1f4283ff")
                WeatherEntity(
                    id = w.id,
                    city = w.name,
                    temp = w.main.temp,
                    humidity = w.main.humidity,
                    condition = w.weather.firstOrNull()?.main ?: "N/A"
                )
            } catch (e: Exception) {
                null
            }
        }

        dao.insertAll(apiList)
        return apiList
    }

    suspend fun getWeatherById(id: Int): WeatherEntity? = dao.getWeather(id)
}