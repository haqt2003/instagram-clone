package com.example.instagram.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagram.data.models.response.GetUserResponse
import com.example.instagram.data.models.response.UpdateUserResponse
import com.example.instagram.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    fun updateUser(
        userId: RequestBody,
        oldPassword: RequestBody?,
        newPassword: RequestBody?,
        name: RequestBody?,
        avatar: MultipartBody.Part?,
        gender: RequestBody?,
        address: RequestBody?,
        introduce: RequestBody?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                userRepository.updateUser(
                    userId,
                    oldPassword,
                    newPassword,
                    name,
                    avatar,
                    gender,
                    address,
                    introduce
                )
            }.onSuccess {
                _updateUser.postValue(it.data)
                _updateMsg.postValue(it.message ?: "Cập nhật thành công!")
            }
        }
    }
}