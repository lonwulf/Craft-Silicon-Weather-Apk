package com.lonwulf.craft_silicon.weatherapp.data.repository

import android.content.Context
import androidx.datastore.dataStore
import com.lonwulf.craft_silicon.weatherapp.data.util.AppSettingsSerializer
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherHistoryPreferences
import com.lonwulf.craft_silicon.weatherapp.domain.repository.DataStoreRepository
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(private val context: Context) : DataStoreRepository {
    private val Context.weatherHistoryDataStore by dataStore(
        "weather-settings.json",
        AppSettingsSerializer
    )

    override val weatherHistory: Flow<List<WeatherHistoryPreferences>> =
        context.weatherHistoryDataStore.data
            .map { preferences ->
                preferences.history
            }

    override suspend fun addWeatherHistory(weatherItem: WeatherHistoryPreferences) {
        context.weatherHistoryDataStore.updateData { preferences ->
            preferences.copy(history = preferences.history.mutate {
                it.add(
                    WeatherHistoryPreferences(
                        windSpeed = weatherItem.windSpeed,
                        humidity = weatherItem.humidity,
                        temp = weatherItem.temp,
                        visibility = weatherItem.visibility,
                        feelsLike = weatherItem.feelsLike,
                        tempMax = weatherItem.tempMax,
                        tempMin = weatherItem.tempMin,
                        weather = weatherItem.weather,
                    )
                )
            })
        }
    }

    override suspend fun clearWeatherHistory() {
        context.weatherHistoryDataStore.updateData { preferences ->
            preferences.copy(history = preferences.history.clear())
        }
    }
}