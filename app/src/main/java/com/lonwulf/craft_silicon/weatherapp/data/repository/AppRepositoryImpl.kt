package com.lonwulf.craft_silicon.weatherapp.data.repository

import com.lonwulf.craft_silicon.weatherapp.core.util.APIResult
import com.lonwulf.craft_silicon.weatherapp.data.source.AppRemoteDataSource
import com.lonwulf.craft_silicon.weatherapp.domain.mapper.toDomainModel
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherModel
import com.lonwulf.craft_silicon.weatherapp.domain.repository.AppRepository
import kotlinx.coroutines.Dispatchers

class AppRepositoryImpl(private val source: AppRemoteDataSource) : AppRepository {
    override suspend fun getWeatherForeCast(
        query: String
    ): APIResult<WeatherModel> {
        return when (val response = source.fetchWeatherForeCast(Dispatchers.IO, query)) {
            is APIResult.Loading -> APIResult.Loading
            is APIResult.Success -> {
                val result = response.result.toDomainModel()
                APIResult.Success(result)
            }

            is APIResult.Error -> APIResult.Error(response.code, response.msg, response.cause)
        }
    }
}
