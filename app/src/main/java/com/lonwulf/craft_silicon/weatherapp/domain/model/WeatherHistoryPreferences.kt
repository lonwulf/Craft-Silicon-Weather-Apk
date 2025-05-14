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

class WeatherHistoryPersistentListSerializer :
    PersistentListSerializer<WeatherHistoryPreferences>(WeatherHistoryPreferences.serializer())

class WeatherListObjectPersistentListSerializer :
    PersistentListSerializer<WeatherPreferences>(WeatherPreferences.serializer())

@Serializable
data class AppSettings(
    @Serializable(with = WeatherHistoryPersistentListSerializer::class)
    val history: PersistentList<WeatherHistoryPreferences> = persistentListOf(),
)

@Serializable
data class WeatherHistoryPreferences(
    val windSpeed: Double,
    val humidity: Int,
    val temp: Double,
    val feelsLike: Double,
    val visibility: Int,
    val tempMin: Double,
    val tempMax: Double,
    @Serializable(with = WeatherListObjectPersistentListSerializer::class)
    val weather: PersistentList<WeatherPreferences> = persistentListOf()
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
)
