package com.example.instagram.data.models

data class PostData(
    val _id: String,
    val author: AuthorData,
    val images: List<String>,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val listLike: List<AuthorData>,
    val totalLike: Int
)
