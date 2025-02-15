package com.example.instagram.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagram.data.models.PostData
import com.example.instagram.data.models.response.AuthResponse
import com.example.instagram.data.models.response.GetUserResponse
import com.example.instagram.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<GetUserResponse?>()
    val user: MutableLiveData<GetUserResponse?> get() = _user

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                userRepository.getUser(username)
            }.onSuccess {
                _user.postValue(it.data)
            }
        }
    }
}