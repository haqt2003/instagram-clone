package com.example.instagram.repositories

import com.example.instagram.data.models.request.UpdateUserRequest
import com.example.instagram.data.models.response.ApiResponse
import com.example.instagram.data.models.response.GetUserResponse
import com.example.instagram.data.models.response.UpdateUserResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface UserRepository {
    @GET("users/{username}")
    suspend fun getUser(@Path("username") username: String): ApiResponse<GetUserResponse>

    @PATCH("users")
    suspend fun updateUser(@Body updateUserRequest: UpdateUserRequest): ApiResponse<UpdateUserResponse>
}