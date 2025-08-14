package com.example.mobileapp

import androidx.contentpager.content.Query

interface WeatherApiService {
    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") query: String, // должно быть "Moscow"
        @Query("lang") lang: String = "ru"
    ): WeatherResponse
}


