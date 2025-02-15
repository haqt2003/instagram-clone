package com.example.instagram.data.models.request

import com.example.instagram.data.enums.Gender
import java.io.File

data class UpdateUserRequest(
    val old_password: String,
    val new_password: String,
    val name: String,
    val avatar: File,
    val gender: Gender,
    val address: String,
    val introduce: String,
    val userId: String
)
