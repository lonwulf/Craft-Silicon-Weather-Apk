package com.lonwulf.craft_silicon.weatherapp.data.source

import com.lonwulf.craft_silicon.weatherapp.data.response.WeatherDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("forecast")
    suspend fun getWeatherForeCast(
        @Query("q") query: String,
        @Query("appid") appid: String
    ): WeatherDTO
}
