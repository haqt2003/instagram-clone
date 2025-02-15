package com.example.instagram.data.models.response

import com.example.instagram.data.models.AuthorData

data class PostResponse(
    val _id: String,
    val author: AuthorData,
    val image: List<String>,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
)
