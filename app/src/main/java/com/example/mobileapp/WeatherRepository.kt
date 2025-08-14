package com.example.mobileapp

class WeatherRepository(private val apiService: WeatherApiService) {
    suspend fun getWeatherByCoordinates(lat: Double, lon: Double): WeatherResponse {
        // Формат запроса: "широта,долгота"
        return apiService.getCurrentWeather(
            apiKey = "3ba42c4971284b86a12220114251308", // ключ
            query = "$lat,$lon"
        )
    }

    suspend fun getWeatherByCity(city: String): WeatherResponse {
        return apiService.getCurrentWeather(
            apiKey = "3ba42c4971284b86a12220114251308", // ключ
            query = city
        )
    }
}