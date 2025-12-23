package com.example.room

import androidx.room.*

@Dao
interface WeatherDao {

    @Query("SELECT * FROM weather")
    suspend fun getAll(): List<WeatherEntity>

    @Query("SELECT * FROM weather WHERE id = :id")
    suspend fun getWeather(id: Int): WeatherEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<WeatherEntity>)
}
