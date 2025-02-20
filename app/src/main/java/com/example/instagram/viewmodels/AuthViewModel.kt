package com.example.instagram.viewmodels

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

    private val _authMsg = MutableLiveData<String?>()
    val authMsg: MutableLiveData<String?> get() = _authMsg

    fun login(username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                authRepository.login(LoginRequest(username, password))
            }.onSuccess {
                _user.postValue(it.data)
                _authMsg.postValue(it.message)
            }
        }
    }

    fun signup(name: String, username: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                authRepository.signup(SignUpRequest(name, username, password))
            }.onSuccess {
                _authMsg.postValue(it.message)
            }
        }

    }
}