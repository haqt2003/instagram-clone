package com.example.instagram.utils

import android.content.Context
import android.content.Intent
import com.example.instagram.ui.MainActivity
import java.util.Locale

fun changeLanguage(context: Context, languageCode: String) {
    val sharedPreferences = context.getSharedPreferences("instagram_config", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putString("language", languageCode)
    editor.apply()

    val locale = Locale(languageCode)
    Locale.setDefault(locale)

    val config = context.resources.configuration
    config.setLocale(locale)

    context.resources.updateConfiguration(config, context.resources.displayMetrics)

    val intent = Intent(context, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    context.startActivity(intent)
}
