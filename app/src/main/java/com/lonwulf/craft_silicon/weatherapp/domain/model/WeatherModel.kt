package com.lonwulf.craft_silicon.weatherapp.domain.model

data class WeatherModel(
    val cod: String,
    val message: Int ,
    val cnt: Int,
    val id: Int,
    val name: String,
    val lat: Double,
    val lon: Double,
    val country: String,
    val population: Int,
    val timezone: Int,
    val sunrise: Int,
    val sunset: Int,
    val weatherList: List<WeatherList>
) {
    data class WeatherList(
        val dt: Int,
        val temp: Double,
        val feelsLike: Double,
        val tempMin: Double,
        val tempMax: Double,
        val pressure: Int,
        val seaLevel: Int,
        val groundLevel: Int,
        val humidity: Int,
        val tempKf: Int,
        val weather: List<Weather> ,
        val clouds: Int,
        val windSpeed: Double,
        val windDeg: Int,
        val windGust: Double,
        val visibility: Int,
        val pop: Int,
        val sys: String,
        val dtTxt: String,
        val rain: Double
    ) {
        data class Weather(
            val id: Int,
            val main: String,
            val description: String,
            val icon: String
        )
    }
}
