package com.example.instagram.utils

import java.text.SimpleDateFormat
import java.util.*

fun formatDate(dateTimeString: String): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    val outputFormat = SimpleDateFormat("d 'tháng' M, yyyy", Locale.getDefault())

    val date = inputFormat.parse(dateTimeString)

    return if (date != null) {
        "ngày ${outputFormat.format(date)}"
    } else {
        "Không xác định"
    }
}

