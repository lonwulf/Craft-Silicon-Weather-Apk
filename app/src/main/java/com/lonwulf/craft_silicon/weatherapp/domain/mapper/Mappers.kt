package com.lonwulf.craft_silicon.weatherapp.domain.mapper

import com.lonwulf.craft_silicon.weatherapp.data.response.WeatherDTO
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherModel
import com.lonwulf.craft_silicon.weatherapp.domain.model.WeatherPreferences

fun WeatherDTO.toDomainModel(): WeatherModel =
    WeatherModel(
        cod = this.cod,
        message = this.message,
        cnt = this.cnt,
        id = this.city?.id ?: 0,
        name = this.city?.name ?: "",
        lat = this.city?.coord?.lat ?: 0.0,
        lon = this.city?.coord?.lon ?: 0.0,
        country = this.city?.country ?: "",
        population = this.city?.population ?: 0,
        timezone = this.city?.timezone ?: 0,
        sunrise = this.city?.sunrise ?: 0,
        sunset = this.city?.sunset ?: 0,
        weatherList = this.list.toWeatherList()
    )

private fun Array<WeatherDTO.WeatherListDTO>.toWeatherList(): List<WeatherModel.WeatherList> =
    mutableListOf<WeatherModel.WeatherList>().apply {
        this@toWeatherList.map {
            add(
                WeatherModel.WeatherList(
                    dt = it.dt,
                    temp = it.main?.temp ?: 0.0,
                    feelsLike = it.main?.feels_like ?: 0.0,
                    tempMin = it.main?.temp_min ?: 0.0,
                    tempMax = it.main?.temp_max ?: 0.0,
                    pressure = it.main?.pressure ?: 0,
                    seaLevel = it.main?.sea_level ?: 0,
                    groundLevel = it.main?.grnd_level ?: 0,
                    humidity = it.main?.humidity ?: 0,
                    tempKf = it.main?.temp_kf ?: 0,
                    clouds = it.clouds?.all ?: 0,
                    windGust = it.wind?.gust ?: 0.0,
                    windDeg = it.wind?.deg ?: 0,
                    visibility = it.visibility,
                    pop = it.pop,
                    sys = it.sys?.pod ?: "",
                    dtTxt = it.dt_txt,
                    rain = it.rain?._3h ?: 0.0,
                    windSpeed = it.wind?.speed ?: 0.0,
                    weather = it.weather.toWeatherDomain()
                )
            )
        }
    }

private fun Array<WeatherDTO.WeatherListDTO.WeatherObjDTO>.toWeatherDomain(): List<WeatherModel.WeatherList.Weather> =
    mutableListOf<WeatherModel.WeatherList.Weather>().apply {
        this@toWeatherDomain.map {
            add(
                WeatherModel.WeatherList.Weather(
                    id = it.id,
                    main = it.main,
                    description = it.description,
                    icon = it.icon
                )
            )
        }
    }

fun List<WeatherModel.WeatherList.Weather>.toWeatherPreferenceList(): List<WeatherPreferences> =
    mutableListOf<WeatherPreferences>().apply {
        this@toWeatherPreferenceList.map {
            add(
                WeatherPreferences(
                    id = it.id,
                    main = it.main,
                    description = it.description,
                    icon = it.icon
                )
            )
        }
    }