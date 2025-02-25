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
        val outputFormat = SimpleDateFormat("MMMM, yyyy", Locale.ENGLISH)
        val calendar = Calendar.getInstance()
        calendar.time = date

        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val monthYear = outputFormat.format(date)
        val dayWithSuffix = "$day${getDaySuffix(day)}"

        "$dayWithSuffix $monthYear"
    } else {
        val outputFormat = SimpleDateFormat("d 'tháng' M, yyyy", Locale.getDefault())
        "ngày ${outputFormat.format(date)}"
    }
}

private fun getDaySuffix(day: Int): String {
    return when {
        day in 11..13 -> "th"
        day % 10 == 1 -> "st"
        day % 10 == 2 -> "nd"
        day % 10 == 3 -> "rd"
        else -> "th"
    }
}
