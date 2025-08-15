package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

// Retrofit API Interface
interface WeatherApi {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"  // Celsius এ temperature পাবার জন্য
    ): WeatherResponse
}
