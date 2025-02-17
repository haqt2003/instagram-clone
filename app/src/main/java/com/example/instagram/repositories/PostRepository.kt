package com.example.instagram.repositories

import com.example.instagram.data.enums.Sort
import com.example.instagram.data.models.request.DeletePostRequest
import com.example.instagram.data.models.request.LikePostRequest
import com.example.instagram.data.models.response.AllPostsResponse
import com.example.instagram.data.models.response.ApiResponse
import com.example.instagram.data.models.response.PostResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
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

    @GET("list-post/{username}")
    suspend fun getAllUserPosts(
        @Path("username") username: String,
    ): ApiResponse<AllPostsResponse>

    @Multipart
    @POST("post")
    suspend fun addPost(
        @Part("userId") userId: RequestBody,
        @Part images: List<MultipartBody.Part>,
        @Part("content") content: RequestBody?
    ): ApiResponse<PostResponse>

    @POST("like")
    suspend fun likePost(
        @Body likePostRequest: LikePostRequest
    ): ApiResponse<Any>

    @Multipart
    @PATCH("post")
    suspend fun editPost(
        @Part("userId") userId: RequestBody,
        @Part("postId") postId: RequestBody,
        @Part images: List<MultipartBody.Part>,
        @Part("content") content: RequestBody?
    ): ApiResponse<PostResponse>

    @HTTP(method = "DELETE", path = "post", hasBody = true)
    suspend fun deletePost(
        @Body deletePostRequest: DeletePostRequest
    ): ApiResponse<Any>


}