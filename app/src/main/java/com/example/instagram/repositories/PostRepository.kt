package com.example.instagram.repositories

import com.example.instagram.data.enums.Sort
import com.example.instagram.data.models.request.LikePostRequest
import com.example.instagram.data.models.response.AllPostsResponse
import com.example.instagram.data.models.response.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface PostRepository {
    @GET("list-post")
    suspend fun getAllPosts(
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int): ApiResponse<AllPostsResponse>

    @GET("list-post/{username}")
    suspend fun getUserPosts(
        @Path("username") username: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("perPage") perPage: Int
        ): ApiResponse<AllPostsResponse>

    @POST("like")
    suspend fun likePost(
        @Body likePostRequest: LikePostRequest
    ): ApiResponse<Any>
}