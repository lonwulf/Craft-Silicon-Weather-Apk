package com.lonwulf.craft_silicon.weatherapp.data.repository

import android.content.Context
import androidx.datastore.dataStore
import com.lonwulf.craft_silicon.weatherapp.data.util.AppSettingsSerializer
import com.lonwulf.craft_silicon.weatherapp.domain.model.AppSettings
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherPreferences
import com.lonwulf.craft_silicon.weatherapp.domain.repository.DataStoreRepository
import kotlinx.collections.immutable.mutate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(private val context: Context) : DataStoreRepository {
    private val Context.weatherHistoryDataStore by dataStore(
        "weather-settings.json",
        AppSettingsSerializer
    )

    override val weatherHistory: Flow<List<WeatherPreferences>> =
        context.weatherHistoryDataStore.data
            .map { preferences ->
                preferences.history
            }

    override val weatherSettings: Flow<AppSettings> = context.weatherHistoryDataStore.data
        .map { it }

    override suspend fun addWeatherHistory(weatherItem: AppSettings) {
        context.weatherHistoryDataStore.updateData { preferences ->
            preferences.copy(
                name = weatherItem.name,
                lat = weatherItem.lat,
                lon = weatherItem.lon,
                country = weatherItem.country,
                population = weatherItem.population,
                timezone = weatherItem.timezone,
                sunset = weatherItem.sunset,
                sunrise = weatherItem.sunrise,
                history = weatherItem.history.mutate {
                    it.addAll(weatherItem.history)
                }
            )
        }
    }

    override suspend fun clearWeatherHistory() {
        context.weatherHistoryDataStore.updateData { preferences ->
            preferences.copy(history = preferences.history.clear())
        }
    }
}