package com.example.mobileapp

// данные для WeatherAPI
data class WeatherResponse(
    val location: Location,
    val current: Current
)

data class Location(
    val name: String,
    val country: String
)

data class Current(
    val temp_c: Double,
    val humidity: Int,
    val wind_kph: Double,
    val wind_dir: String,
    val cloud: Int,
    val condition: Condition
)

data class Condition(
    val text: String
)