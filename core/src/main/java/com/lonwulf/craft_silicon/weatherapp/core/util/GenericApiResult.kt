package com.lonwulf.craft_silicon.weatherapp.core.util

data class GenericApiResult<out T>(
    val results: T,
    val isSuccessful: Boolean,
    val msg: String? = "success"
)
