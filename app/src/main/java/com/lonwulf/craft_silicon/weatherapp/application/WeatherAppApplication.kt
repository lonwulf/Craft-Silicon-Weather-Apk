package com.lonwulf.craft_silicon.weatherapp.application

import android.app.Application
import com.lonwulf.craft_silicon.weatherapp.core.di.networkModule
import com.lonwulf.craft_silicon.weatherapp.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherAppApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@WeatherAppApplication)
            modules(appModule, networkModule)
        }
    }
}