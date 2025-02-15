package com.example.instagram.data.models.response

import com.example.instagram.data.models.PostData

data class AllPostsResponse(
    val page: Int,
    val totalPage: Int,
    val totalPost: Int,
    val data: List<PostData>
)
