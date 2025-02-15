package com.example.instagram

import android.app.Application
import com.example.instagram.di.repositoryModule
import com.example.instagram.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ApplicationConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationConfig)
            modules(listOf(repositoryModule,viewModelModule))
        }
    }
}