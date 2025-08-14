package com.example.mobileapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.mobileapp.ui.theme.MobileAppTheme
import java.util.jar.Manifest
import com.example.mobileapp.permission

class MainActivity : ComponentActivity() {
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var locationHelper: LocationHelper

    @SuppressLint("MissingSuperCall")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationHelper = LocationHelper(this)

        viewModel.weatherData.observe(this) { weather ->
            displayWeather(weather)
        }

        viewModel.errorMessage.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }

        fun requestLocation() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fetchLocation()
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_CODE
                )
            }
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

        requestLocation()
    }

    private fun requestLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLocation()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE && grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            fetchLocation()
        } else {
            viewModel.fetchWeather() // Загружаем погоду для Москвы
        }
    }

    private fun fetchLocation() {
        locationHelper.getLastLocation(
            onSuccess = { location ->
                viewModel.fetchWeather(location.latitude, location.longitude)
            },
            onFailure = {
                viewModel.fetchWeather() // Москва по умолчанию
            }
        )
    }

    private fun displayWeather(weather: WeatherResponse) {
        try {
            // Данные из API
            val city = "${weather.location.name}, ${weather.location.country}"
            val temp = weather.current.temp_c
            val humidity = weather.current.humidity
            val windSpeed = weather.current.wind_kph
            val windDir = weather.current.wind_dir // Уже строка с направлением
            val cloudiness = weather.current.cloud
            val condition = weather.current.condition.text

            // Обновляем UI
            findViewById<TextView>(R.id.tvCity).text = "Город: $city"
            findViewById<TextView>(R.id.tvTemp).text = "Температура: $temp°C"
            findViewById<TextView>(R.id.tvHumidity).text = "Влажность: $humidity%"
            findViewById<TextView>(R.id.tvWind).text = "Ветер: ${windSpeed} км/ч, $windDir"
            findViewById<TextView>(R.id.tvClouds).text = "Облачность: $cloudiness%"
            findViewById<TextView>(R.id.tvPrecipitation).text = "Состояние: $condition"
        } catch (e: Exception) {
            Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_CODE = 1001
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileAppTheme {
        Greeting("Android")
    }
}