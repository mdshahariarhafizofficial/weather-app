package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    WeatherScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun WeatherScreen(modifier: Modifier = Modifier) {
    var city by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }
    val apiKey = "99d25c11f8d420ab319f383bfd113fab"

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("Enter City Name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (city.isNotEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = RetrofitInstance.api.getWeather(city, apiKey)
                            withContext(Dispatchers.Main) {
                                result = "City: ${response.name}\n" +
                                        "Temp: ${response.main.temp}°C\n" +
                                        "Humidity: ${response.main.humidity}%\n" +
                                        "Condition: ${response.weather[0].description}"
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                result = "Error: ${e.message}"
                            }
                        }
                    }
                } else {
                    result = "Please enter a city name."
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Weather")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = result)
    }
}
