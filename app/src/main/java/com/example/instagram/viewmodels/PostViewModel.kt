package com.example.instagram.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagram.data.enums.LikeValue
import com.example.instagram.data.enums.Sort
import com.example.instagram.data.models.PostData
import com.example.instagram.data.models.request.DeletePostRequest
import com.example.instagram.data.models.request.LikePostRequest
import com.example.instagram.repositories.PostRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {
    private val _posts = MutableLiveData<List<PostData>>()
    val posts: LiveData<List<PostData>> get() = _posts

    private val _userPosts = MutableLiveData<List<PostData>>()
    val userPosts: LiveData<List<PostData>> get() = _userPosts

    private val _msg = MutableLiveData<String>()
    val msg: LiveData<String> get() = _msg

    private val _hasMoreData = MutableLiveData<Boolean>()
    val hasMoreData: MutableLiveData<Boolean> get() = _hasMoreData

    private val _hasMoreDataUser = MutableLiveData<Boolean>()
    val hasMoreDataUser: MutableLiveData<Boolean> get() = _hasMoreDataUser

    private val _currentPageUserPost = MutableLiveData<Int>()
    val currentPageUserPost: MutableLiveData<Int> get() = _currentPageUserPost

    private val _likePostUpdated = MutableLiveData<Boolean>()
    val likePostUpdated: LiveData<Boolean> get() = _likePostUpdated

    private val _reloadHome = MutableLiveData<Boolean>()
    val reloadHome: LiveData<Boolean> get() = _reloadHome

    fun getPosts(pageNumber: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.getAllPosts(Sort.NEWEST.value, pageNumber, 10)
            }.onSuccess {
                if (pageNumber > 1) {
                    val newData = it.data?.data ?: emptyList()
                    val currentData = _posts.value ?: emptyList()
                    val updatedData = currentData + newData
                    _posts.postValue(updatedData)
                    _hasMoreData.postValue(newData.isNotEmpty())
                } else {
                    _posts.postValue(it.data?.data ?: emptyList())
                    _hasMoreData.postValue(it.data?.data?.isNotEmpty() ?: false)
                }
            }
        }
    }

    fun getUserPosts(username: String, pageNumber: Int, perPage: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.getUserPosts(username, Sort.NEWEST.value, pageNumber, perPage)
            }.onSuccess {
                if (pageNumber > 1) {
                    val newData = it.data?.data ?: emptyList()
                    val currentData = _userPosts.value ?: emptyList()
                    val updatedData = currentData + newData
                    _userPosts.postValue(updatedData)
                    _hasMoreDataUser.postValue(newData.isNotEmpty())
                    _currentPageUserPost.postValue(pageNumber)
                } else {
                    _userPosts.postValue(it.data?.data ?: emptyList())
                    _hasMoreDataUser.postValue(it.data?.data?.isNotEmpty() ?: false)
                    _currentPageUserPost.postValue(pageNumber)
                }
            }
        }
    }

    fun addPost(userId: RequestBody, images: List<MultipartBody.Part>, content: RequestBody?) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.addPost(userId, images, content)
            }.onSuccess {
                _msg.postValue(it.message ?: "Thêm bài viết thành công!")
            }
        }
    }

    fun editPost(
        userId: RequestBody,
        postId: RequestBody,
        images: List<MultipartBody.Part>,
        content: RequestBody?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.editPost(userId, postId, images, content)
            }.onSuccess {
                _msg.postValue(it.message ?: "Sửa bài viết thành công!")
            }
        }
    }

    fun likePost(userId: String, postId: String, like: Boolean, isNotHome: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                if (like) {
                    postRepository.likePost(LikePostRequest(userId, postId, LikeValue.LIKE.value))
                } else {
                    postRepository.likePost(
                        LikePostRequest(
                            userId,
                            postId,
                            LikeValue.DISLIKE.value
                        )
                    )
                }

                if (isNotHome) {
                    _likePostUpdated.postValue(true)
                }
            }
        }
    }

    fun deletePost(userId: String, postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.deletePost(DeletePostRequest(userId, postId))
            }.onSuccess {
                _msg.postValue(it.message.toString())
                _userPosts.postValue(_userPosts.value?.filter { post -> post._id != postId })
            }
        }
    }

    fun getAllUserPosts(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.getAllUserPosts(username)
            }.onSuccess {
                _userPosts.postValue(it.data?.data ?: emptyList())
            }
        }
    }

    fun setReloadHome(reload: Boolean) {
        _reloadHome.postValue(reload)
    }
}