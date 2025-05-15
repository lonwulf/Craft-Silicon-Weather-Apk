package com.lonwulf.craft_silicon.weatherapp.domain.repository

import com.lonwulf.craft_silicon.weatherapp.domain.model.AppSettings
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherPreferences
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    val weatherHistory: Flow<List<WeatherPreferences>>
    val weatherSettings: Flow<AppSettings>
    suspend fun addWeatherHistory(weatherItem: AppSettings)
    suspend fun clearWeatherHistory()
}