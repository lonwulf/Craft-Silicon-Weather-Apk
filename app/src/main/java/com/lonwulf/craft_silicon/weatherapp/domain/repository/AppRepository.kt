package com.lonwulf.craft_silicon.weatherapp.domain.repository

import com.lonwulf.craft_silicon.weatherapp.core.util.APIResult
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherModel

interface AppRepository {
    suspend fun getWeatherForeCast(
        query: String
    ): APIResult<WeatherModel>

}
