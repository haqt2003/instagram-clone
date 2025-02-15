package com.example.instagram.data.models.request

import com.example.instagram.data.enums.LikeValue

data class LikePostRequest(val userId: String, val postId: String, val likeValue: Int)
