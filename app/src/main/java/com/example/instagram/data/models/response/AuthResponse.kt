package com.example.instagram.data.models.response

import com.example.instagram.data.enums.Gender

data class AuthResponse(
    val _id: String,
    val username: String,
    val password: String,
    val name: String,
    val avatar: String,
    val gender: Gender,
    val address: String,
    val introduce: String,
)
