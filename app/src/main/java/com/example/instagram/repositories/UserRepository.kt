package com.example.instagram.repositories

import com.example.instagram.data.models.response.ApiResponse
import com.example.instagram.data.models.response.GetUserResponse
import com.example.instagram.data.models.response.UpdateUserResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path

interface UserRepository {
    @GET("user/{username}")
    suspend fun getUser(@Path("username") username: String): ApiResponse<GetUserResponse>

    @Multipart
    @PATCH("user")
    suspend fun updateUser(
        @Part("userId") userId: RequestBody,
        @Part("old_password") oldPassword: RequestBody?,
        @Part("new_password") newPassword: RequestBody?,
        @Part("name") name: RequestBody?,
        @Part avatar: MultipartBody.Part?,
        @Part("gender") gender: RequestBody?,
        @Part("address") address: RequestBody?,
        @Part("introduce") introduce: RequestBody?
    ): ApiResponse<UpdateUserResponse>
}