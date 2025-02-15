package com.example.instagram.data.models

import com.example.instagram.data.enums.Gender

data class AuthorData(
    val username: String,
    val name: String,
    val avatar: String,
    val gender: Gender,
    val address: String,
    val introduce: String,
)
