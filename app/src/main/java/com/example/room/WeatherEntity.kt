package com.example.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey val id: Int,
    val city: String,
    val temp: Double,
    val humidity: Int,
    val condition: String
)
