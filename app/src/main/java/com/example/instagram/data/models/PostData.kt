package com.example.instagram.data.models

import java.io.Serializable

data class PostData(
    val _id: String,
    val author: AuthorData,
    val images: List<String>,
    val content: String,
    val createdAt: String,
    val updatedAt: String,
    val listLike: MutableList<AuthorData>,
    var totalLike: Int
) : Serializable
