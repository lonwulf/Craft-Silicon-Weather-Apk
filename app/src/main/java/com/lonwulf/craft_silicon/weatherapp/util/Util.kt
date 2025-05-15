package com.lonwulf.craft_silicon.weatherapp.util

import android.icu.text.NumberFormat
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.UtcOffset
import kotlinx.datetime.toLocalDateTime
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

fun thousandFormatter(value: Int): String {
    val formatter = NumberFormat.getNumberInstance(Locale.ROOT)
    return formatter.format(value)
}

fun convertToLocalTime(timestamp: Int, timezoneOffsetSeconds: Int): LocalTime {
    val instant = Instant.fromEpochSeconds(timestamp.toLong())
    val adjustedInstant = instant.plus(timezoneOffsetSeconds.seconds)
    val localDateTime = adjustedInstant.toLocalDateTime(TimeZone.UTC)
    return localDateTime.time
}

fun timezoneToUtcOffset(timezone: Int): UtcOffset {
    return UtcOffset(seconds = timezone)
}

fun formatVisibility(meters: Int): String {
    return if (meters >= 1000) {
        val km = meters / 1000.0
        "%.1f km".format(km)
    } else {
        "$meters meters"
    }
}
fun formatPressure(hPa: Int): String {
    return when {
        hPa < 1000 -> "$hPa hPa (Low pressure - possible rain)"
        hPa in 1000..1020 -> "$hPa hPa (Normal)"
        else -> "$hPa hPa (High pressure - clear skies)"
    }
}
fun formatWindSpeed(speedMps: Double): String {
    val kmh = speedMps * 3.6
    return "%.1f m/s (%.1f km/h)".format(speedMps, kmh)
}