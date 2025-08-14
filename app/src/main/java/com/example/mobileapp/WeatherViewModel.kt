package com.example.mobileapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository(RetrofitClient.apiService)
    val weatherData = MutableLiveData<WeatherResponse>()
    val errorMessage = MutableLiveData<String>()

    fun fetchWeather(lat: Double? = null, lon: Double? = null) {
        viewModelScope.launch {
            try {
                val response = if (lat != null && lon != null) {
                    repository.getWeatherByCoordinates(lat, lon)
                } else {
                    repository.getWeatherByCity("Moscow")
                }
                weatherData.value = response
            } catch (e: Exception) {
                errorMessage.value = "Ошибка: ${e.message}"
            }
        }
    }
}