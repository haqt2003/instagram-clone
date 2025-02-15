package com.example.instagram.repositories

import android.content.Context
import com.example.instagram.data.models.request.LoginRequest
import com.example.instagram.data.models.request.SignUpRequest
import com.example.instagram.data.models.response.ApiResponse
import com.example.instagram.data.models.response.AuthResponse
import com.example.instagram.retrofit.AuthApiService

class AuthRepositoryImpl(private val context: Context) : AuthRepository {
    private val authApiService = AuthApiService.authService

    override suspend fun login(loginRequest: LoginRequest): ApiResponse<AuthResponse> {
        return authApiService.login(loginRequest)
    }

    override suspend fun signup(signUpRequest: SignUpRequest): ApiResponse<AuthResponse> {
        return authApiService.signup(signUpRequest)
    }
}