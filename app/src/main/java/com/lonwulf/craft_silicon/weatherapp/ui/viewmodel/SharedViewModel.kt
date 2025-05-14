package com.lonwulf.craft_silicon.weatherapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lonwulf.craft_silicon.weatherapp.core.util.GenericResultState
import com.lonwulf.craft_silicon.weatherapp.domain.mapper.toWeatherPreferenceList
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherHistoryPreferences
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherModel
import com.lonwulf.craft_silicon.weatherapp.domain.usecase.FetchHistoryFromCacheUseCase
import com.lonwulf.craft_silicon.weatherapp.domain.usecase.WeatherForeCastUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SharedViewModel(
    private val weatherForeCastUseCase: WeatherForeCastUseCase,
    private val fetchHistoryFromCacheUseCase: FetchHistoryFromCacheUseCase
) : ViewModel() {
    private var _weatherForeCastStateFlow =
        MutableStateFlow<GenericResultState<WeatherModel>>(GenericResultState.Loading)
    val weatherForeCastStateFlow
        get() = _weatherForeCastStateFlow.asStateFlow()
    private var _weatherPreferencesList =
        MutableStateFlow<GenericResultState<List<WeatherHistoryPreferences>>>(GenericResultState.Loading)
    val weatherPreferencesList
        get() = _weatherPreferencesList.asStateFlow()

    fun fetchWeatherForeCast(
        latitude: Double,
        longitude: Double
    ) = viewModelScope.launch(Dispatchers.IO) {
        weatherForeCastUseCase(latitude = latitude, longitude = longitude).onStart {
            setWeatherForeCastApiResult(GenericResultState.Loading)
        }
            .flowOn(Dispatchers.IO).collect { result ->
                if (result.isSuccessful) {
                    setWeatherForeCastApiResult(GenericResultState.Success(result.result))
                } else {
                    setWeatherForeCastApiResult(GenericResultState.Error(result.msg))
                }
            }

    }

    fun fetchAllHistory() = viewModelScope.launch(Dispatchers.IO) {
        fetchHistoryFromCacheUseCase().onStart { setWeatherHistoryResult(GenericResultState.Loading) }
            .flowOn(Dispatchers.IO).collect {
                setWeatherHistoryResult(GenericResultState.Success(it))
            }
    }

    fun clearAllData() = viewModelScope.launch(Dispatchers.IO) {
        fetchHistoryFromCacheUseCase.clearHistory()
    }

    fun addWeatherHistory(model: WeatherModel.WeatherList) = viewModelScope.launch(Dispatchers.IO) {
        fetchHistoryFromCacheUseCase.addHistory(
            WeatherHistoryPreferences(
                windSpeed = model.windSpeed,
                humidity = model.humidity,
                temp = model.temp,
                visibility = model.visibility,
                feelsLike = model.feelsLike,
                tempMax = model.tempMax,
                tempMin = model.tempMin,
                weather = model.weather.toWeatherPreferenceList().toPersistentList()
            )
        )
    }


    private fun setWeatherForeCastApiResult(data: GenericResultState<WeatherModel>) {
        _weatherForeCastStateFlow.value = data
    }

    private fun setWeatherHistoryResult(data: GenericResultState<List<WeatherHistoryPreferences>>) {
        _weatherPreferencesList.value = data
    }
}
