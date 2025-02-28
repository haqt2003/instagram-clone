package com.example.instagram

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import com.example.instagram.di.repositoryModule
import com.example.instagram.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.Locale

class ApplicationConfig : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@ApplicationConfig)
            modules(listOf(repositoryModule, viewModelModule))
        }
        setAppLanguage()
    }

    private fun setAppLanguage() {
        val sharedPreferences = getSharedPreferences("instagram_config", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("language", "")

        val locale = if (languageCode.toString() == "") {
            Locale.getDefault()
        } else {
            Locale(languageCode.toString())
        }

        Locale.setDefault(locale)

        val config = resources.configuration
        config.setLocale(locale)

        resources.updateConfiguration(config, resources.displayMetrics)
    }
}
