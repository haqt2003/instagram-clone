package com.example.instagram.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagram.data.models.request.LoginRequest
import com.example.instagram.data.models.request.SignUpRequest
import com.example.instagram.data.models.response.AuthResponse
import com.example.instagram.repositories.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _user = MutableLiveData<AuthResponse?>()
    val user: MutableLiveData<AuthResponse?> get() = _user

    private val _isRegisterSuccess = MutableLiveData<Boolean>()
    val isRegisterSuccess: MutableLiveData<Boolean> get() = _isRegisterSuccess

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                authRepository.login(LoginRequest(username, password))
            }.onSuccess {
                _user.postValue(it.data)
            }.onFailure {
                Log.d("Login", it.message ?: "")
            }
        }
    }

    fun signup(name: String, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                authRepository.signup(SignUpRequest(name, username, password))
            }.onSuccess {
                _isRegisterSuccess.postValue(true)
            }.onFailure {
                _isRegisterSuccess.postValue(false)
            }
        }

    }
}