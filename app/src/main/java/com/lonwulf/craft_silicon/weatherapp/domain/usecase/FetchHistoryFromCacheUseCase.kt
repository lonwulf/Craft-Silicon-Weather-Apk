package com.lonwulf.craft_silicon.weatherapp.domain.usecase

import com.lonwulf.craft_silicon.weatherapp.domain.model.AppSettings
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherPreferences
import com.lonwulf.craft_silicon.weatherapp.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class FetchHistoryFromCacheUseCase(private val dataStoreRepository: DataStoreRepository) {
    operator fun invoke(): Flow<List<WeatherPreferences>> =
        dataStoreRepository.weatherHistory

    fun fetchCachedWeatherSettings(): Flow<AppSettings> =
        dataStoreRepository.weatherSettings

    suspend fun clearHistory() = dataStoreRepository.clearWeatherHistory()

    suspend fun addHistory(weatherObject: AppSettings) {
        dataStoreRepository.addWeatherHistory(weatherObject)
    }
}