package com.lonwulf.craft_silicon.weatherapp.domain.model

data class WeatherModel(
    val cod: String = "",
    val message: Int = 0,
    val cnt: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val country: String = "",
    val population: Int = 0,
    val timezone: Int = 0,
    val sunrise: Int = 0,
    val sunset: Int = 0,
    val weatherList: List<WeatherList>
) {
    data class WeatherList(
        val dt: Int = 0,
        val temp: Double = 0.0,
        val feelsLike: Double,
        val tempMin: Double,
        val tempMax: Double,
        val pressure: Int,
        val seaLevel: Int = 0,
        val groundLevel: Int = 0,
        val humidity: Int,
        val tempKf: Double = 0.0,
        val weather: List<Weather>,
        val clouds: Int = 0,
        val windSpeed: Double,
        val windDeg: Int = 0,
        val windGust: Double = 0.0,
        val visibility: Int,
        val pop: Double = 0.0,
        val sys: String = "",
        val dtTxt: String = "",
        val rain: Double = 0.0
    ) {
        data class Weather(
            val id: Int,
            val main: String,
            val description: String,
            val icon: String
        )
    }
}
