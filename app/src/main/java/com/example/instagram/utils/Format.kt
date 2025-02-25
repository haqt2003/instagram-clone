package com.example.instagram.utils

import android.content.Context
import com.example.instagram.R
import java.text.SimpleDateFormat
import java.util.*

fun formatDate(context: Context, dateTimeString: String): String {
    val sharedPreferences = context.getSharedPreferences("instagram_config", Context.MODE_PRIVATE)
    val language = sharedPreferences.getString("language", "vi")

    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    inputFormat.timeZone = TimeZone.getTimeZone("UTC")

    val date = inputFormat.parse(dateTimeString) ?: return context.getString(R.string.undefined)

    return if (language == "en") {
        val outputFormat = SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH)
        outputFormat.format(date)
    } else {
        val outputFormat = SimpleDateFormat("d 'tháng' M, yyyy", Locale.getDefault())
        "ngày ${outputFormat.format(date)}"
    }
}

