package com.lonwulf.craft_silicon.weatherapp.domain.model

import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

open class PersistentListSerializer<T>(elementSerializer: KSerializer<T>) :
    KSerializer<PersistentList<T>> {
    private val listSerializer = ListSerializer(elementSerializer)

    override val descriptor: SerialDescriptor = listSerializer.descriptor

    override fun serialize(encoder: Encoder, value: PersistentList<T>) {
        listSerializer.serialize(encoder, value.toList())
    }

    override fun deserialize(decoder: Decoder): PersistentList<T> {
        return listSerializer.deserialize(decoder).toPersistentList()
    }
}

class WeatherListObjectPersistentListSerializer :
    PersistentListSerializer<WeatherPreferences>(WeatherPreferences.serializer())

class WeatherDetailsListPersistentListSerializer :
    PersistentListSerializer<WeatherDetailsPreferences>(WeatherDetailsPreferences.serializer())


@Serializable
data class AppSettings(
    val name: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val country: String = "",
    val population: Int = 0,
    val timezone: Int = 0,
    val sunrise: Int = 0,
    val sunset: Int = 0,
    @Serializable(with = WeatherListObjectPersistentListSerializer::class)
    val history: PersistentList<WeatherPreferences> = persistentListOf(),
)

@Serializable
data class WeatherPreferences(
    val windSpeed: Double,
    val humidity: Int,
    val temp: Double,
    val feelsLike: Double,
    val visibility: Int,
    val tempMin: Double,
    val tempMax: Double,
    val pressure:Int,
    @Serializable(with = WeatherDetailsListPersistentListSerializer::class)
    val weatherDetails: PersistentList<WeatherDetailsPreferences> = persistentListOf()
)

@Serializable
data class WeatherDetailsPreferences(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
