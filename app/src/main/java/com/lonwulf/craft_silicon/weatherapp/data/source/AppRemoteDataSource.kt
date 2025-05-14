package com.lonwulf.craft_silicon.weatherapp.data.source

import com.lonwulf.craft_silicon.weatherapp.BuildConfig
import com.lonwulf.craft_silicon.weatherapp.core.network.RemoteDataSource
import com.lonwulf.craft_silicon.weatherapp.core.util.APIResult
import com.lonwulf.craft_silicon.weatherapp.data.response.WeatherDTO
import kotlinx.coroutines.CoroutineDispatcher

class AppRemoteDataSource(private val apiService: APIService) : RemoteDataSource() {
    suspend fun fetchWeatherForeCast(
        dispatcher: CoroutineDispatcher,
        query: String
    ): APIResult<WeatherDTO> = safeApiCall(dispatcher) {
        apiService.getWeatherForeCast(
            query = query,
            apiKey = BuildConfig.WEATHER_API_KEY,
            aqi = "no"
        )
    }
}
