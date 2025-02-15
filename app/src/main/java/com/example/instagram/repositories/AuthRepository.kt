package com.example.instagram.repositories

import com.example.instagram.data.models.request.LoginRequest
import com.example.instagram.data.models.request.SignUpRequest
import com.example.instagram.data.models.response.ApiResponse
import com.example.instagram.data.models.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRepository {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse<AuthResponse>

    @POST("signup")
    suspend fun signup(@Body signUpRequest: SignUpRequest): ApiResponse<AuthResponse>
}