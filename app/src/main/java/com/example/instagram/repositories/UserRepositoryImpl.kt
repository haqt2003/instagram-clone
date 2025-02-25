package com.example.instagram.repositories

import android.content.Context
import com.example.instagram.data.models.response.ApiResponse
import com.example.instagram.data.models.response.GetUserResponse
import com.example.instagram.data.models.response.UpdateUserResponse
import com.example.instagram.retrofit.UserApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepositoryImpl(private val context: Context) : UserRepository {
    private val userApiService = UserApiService.userService

    override suspend fun getUser(username: String): ApiResponse<GetUserResponse> {
        return userApiService.getUser(username)
    }

    override suspend fun updateUser(
        userId: RequestBody,
        oldPassword: RequestBody?,
        newPassword: RequestBody?,
        name: RequestBody?,
        avatar: MultipartBody.Part?,
        gender: RequestBody?,
        address: RequestBody?,
        introduce: RequestBody?
    ): ApiResponse<UpdateUserResponse> {
        return userApiService.updateUser(userId, oldPassword, newPassword, name, avatar, gender, address, introduce)
    }
}