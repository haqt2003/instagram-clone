package com.example.instagram.repositories

import android.content.Context
import android.util.Log
import com.example.instagram.data.enums.Sort
import com.example.instagram.data.models.request.LikePostRequest
import com.example.instagram.data.models.response.AllPostsResponse
import com.example.instagram.data.models.response.ApiResponse
import com.example.instagram.retrofit.PostApiService

class PostRepositoryImpl(private val context: Context) : PostRepository {
    private val postApiService = PostApiService.postService

    override suspend fun getAllPosts(
        sort: String,
        page: Int,
        perPage: Int
    ): ApiResponse<AllPostsResponse> {
        return postApiService.getAllPosts(sort, page, perPage)
    }

    override suspend fun getUserPosts(
        username: String,
        sort: String,
        page: Int,
        perPage: Int
    ): ApiResponse<AllPostsResponse> {
        return postApiService.getUserPosts(username, sort, page, perPage)
    }

    override suspend fun likePost(likePostRequest: LikePostRequest): ApiResponse<Any> {
        return postApiService.likePost(likePostRequest)
    }
}