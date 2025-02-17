package com.example.instagram.data.models.request

import java.io.File

data class UpdatePostRequest(
    val userId: String,
    val postId: String,
    val image: List<String>,
    val content: String
)
