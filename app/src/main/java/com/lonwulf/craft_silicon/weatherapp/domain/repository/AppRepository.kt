package com.lonwulf.craft_silicon.weatherapp.domain.repository

import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherModel
import com.lonwulf.craft_silicon.weatherapp.core.util.APIResult

interface AppRepository {
    suspend fun getWeatherForeCast(query:String): APIResult<WeatherModel>

}
