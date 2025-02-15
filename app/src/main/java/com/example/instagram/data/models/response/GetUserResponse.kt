package com.example.instagram.data.models.response

import com.example.instagram.data.enums.Gender

data class GetUserResponse(
    val username: String,
    val name: String,
    val avatar: String,
    val address: String,
    val gender: Gender,
    val introduce: String,
    val totalPost: Int
)