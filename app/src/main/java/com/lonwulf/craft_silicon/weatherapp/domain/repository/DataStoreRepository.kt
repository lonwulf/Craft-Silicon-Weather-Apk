package com.lonwulf.craft_silicon.weatherapp.domain.repository

import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherHistoryPreferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val weatherHistory: Flow<List<WeatherHistoryPreferences>>
    suspend fun addWeatherHistory(weatherItem: WeatherHistoryPreferences)
    suspend fun clearWeatherHistory()
}