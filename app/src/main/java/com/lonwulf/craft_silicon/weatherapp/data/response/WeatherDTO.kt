package com.lonwulf.craft_silicon.weatherapp.data.response

import com.squareup.moshi.Json


data class WeatherDTO(
    @Json(name = "cod") var cod: String = "",
    @Json(name = "message") var message: Int = 0,
    @Json(name = "cnt") var cnt: Int = 0,
    @Json(name = "list") var list: Array<WeatherListDTO> = emptyArray(),
    @Json(name = "city") var city: CityDTO? = null
) {
    data class WeatherListDTO(
        @Json(name = "dt") var dt: Int = 0,
        @Json(name = "main") var main: MainDTO? = null,
        @Json(name = "weather") var weather: Array<WeatherObjDTO> = emptyArray(),
        @Json(name = "clouds") var clouds: CloudsDTO? = null,
        @Json(name = "wind") var wind: WindDTO? = null,
        @Json(name = "visibility") var visibility: Int = 0,
        @Json(name = "pop") var pop: Int = 0,
        @Json(name = "sys") var sys: SysDTO? = null,
        @Json(name = "dt_txt") var dt_txt: String = "",
        @Json(name = "rain") var rain: RainDTO? = null
    ) {
        data class CloudsDTO(
            @Json(name = "all") var all: Int = 0
        )

        data class WindDTO(
            @Json(name = "speed") var speed: Double = 0.0,
            @Json(name = "deg") var deg: Int = 0,
            @Json(name = "gust") var gust: Double = 0.0
        )

        data class WeatherObjDTO(
            @Json(name = "id") var id: Int = 0,
            @Json(name = "main") var main: String = "",
            @Json(name = "description") var description: String = "",
            @Json(name = "icon") var icon: String = ""
        )

        data class SysDTO(
            @Json(name = "pod") var pod: String = ""
        )

        data class RainDTO(
            @Json(name = "3h") var _3h: Double = 0.0
        )

        data class MainDTO(
            @Json(name = "temp") var temp: Double = 0.0,
            @Json(name = "feels_like") var feels_like: Double = 0.0,
            @Json(name = "temp_min") var temp_min: Double = 0.0,
            @Json(name = "temp_max") var temp_max: Double = 0.0,
            @Json(name = "pressure") var pressure: Int = 0,
            @Json(name = "sea_level") var sea_level: Int = 0,
            @Json(name = "grnd_level") var grnd_level: Int = 0,
            @Json(name = "humidity") var humidity: Int = 0,
            @Json(name = "temp_kf") var temp_kf: Int = 0
        )
    }

    data class CityDTO(
        @Json(name = "id") var id: Int = 0,
        @Json(name = "name") var name: String = "",
        @Json(name = "coord") var coord: CoordDTO? = null,
        @Json(name = "country") var country: String = "",
        @Json(name = "population") var population: Int = 0,
        @Json(name = "timezone") var timezone: Int = 0,
        @Json(name = "sunrise") var sunrise: Int = 0,
        @Json(name = "sunset") var sunset: Int = 0
    ) {
        data class CoordDTO(
            @Json(name = "lat") var lat: Double = 0.0,
            @Json(name = "lon") var lon: Double = 0.0
        )
    }
}
