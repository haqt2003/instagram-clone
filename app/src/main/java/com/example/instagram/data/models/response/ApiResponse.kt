package com.example.instagram.data.models.response

data class ApiResponse<T>(
    val status: String,
    val data: T?,
    val message: String?
)
