package com.example.instagram.data.models.response

data class GetUserResponse(
    val username: String,
    val name: String,
    val avatar: String,
    val address: String,
    val gender: String,
    val introduce: String,
    val totalPost: Int
)