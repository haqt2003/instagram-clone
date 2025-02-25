package com.example.instagram.data.models.request

data class LikePostRequest(val userId: String, val postId: String, val likeValue: Int)
