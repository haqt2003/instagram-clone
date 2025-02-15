package com.example.instagram.repositories

import android.content.Context
import com.example.instagram.data.models.request.UpdateUserRequest
import com.example.instagram.data.models.response.ApiResponse
import com.example.instagram.data.models.response.GetUserResponse
import com.example.instagram.data.models.response.UpdateUserResponse
import com.example.instagram.retrofit.UserApiService

class UserRepositoryImpl(private val context: Context) : UserRepository {
    private val userApiService = UserApiService.userService

    override suspend fun getUser(username: String): ApiResponse<GetUserResponse> {
        return userApiService.getUser(username)
    }

    override suspend fun updateUser(updateUserRequest: UpdateUserRequest): ApiResponse<UpdateUserResponse> {
        return userApiService.updateUser(updateUserRequest)
    }

}