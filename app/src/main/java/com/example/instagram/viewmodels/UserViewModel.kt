package com.example.instagram.viewmodels

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagram.data.models.PostData
import com.example.instagram.data.models.request.UpdateUserRequest
import com.example.instagram.data.models.response.AuthResponse
import com.example.instagram.data.models.response.GetUserResponse
import com.example.instagram.data.models.response.UpdateUserResponse
import com.example.instagram.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _user = MutableLiveData<GetUserResponse?>()
    val user: MutableLiveData<GetUserResponse?> get() = _user

    private val _updateUser = MutableLiveData<UpdateUserResponse?>()
    val updateUser: MutableLiveData<UpdateUserResponse?> get() = _updateUser

    private val _updateMsg = MutableLiveData<String?>()
    val updateMsg: MutableLiveData<String?> get() = _updateMsg

    fun getUser(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                userRepository.getUser(username)
            }.onSuccess {
                _user.postValue(it.data)
            }
        }
    }

    fun updateUser(userRequest: UpdateUserRequest) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                userRepository.updateUser(userRequest)
            }.onSuccess {
                _updateUser.postValue(it.data)
                _updateMsg.postValue(it.message)
            }
        }
    }
}