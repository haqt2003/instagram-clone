package com.example.instagram.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagram.data.enums.LikeValue
import com.example.instagram.data.enums.Sort
import com.example.instagram.data.models.AuthorData
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

    private val _msg = MutableLiveData<String?>()
    val msg: MutableLiveData<String?> get() = _msg

//    private val _hasMoreData = MutableLiveData<Boolean>()
//    val hasMoreData: MutableLiveData<Boolean> get() = _hasMoreData

    fun getPosts(pageNumber: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.getAllPosts(Sort.NEWEST.value, pageNumber, 10)
            }.onSuccess {
                _posts.postValue(it.data?.data ?: listOf())
            }.onFailure {
                // Handle error
            }
        }
    }

    fun getUserPosts(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.getUserPosts(username, Sort.NEWEST.value, 1, 10)
            }.onSuccess {
                _userPosts.postValue(it.data?.data ?: listOf())
            }.onFailure {
                // Handle error
            }
        }
    }

    fun getAllUserPosts(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.getAllUserPosts(username)
            }.onSuccess {
                _userPosts.postValue(it.data?.data ?: listOf())
            }
        }
    }

    fun addPost(userId: RequestBody, images: List<MultipartBody.Part>, content: RequestBody?) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.addPost(userId, images, content)
            }.onSuccess {
                _msg.postValue(it.message)
            }
        }
    }

    fun editPost(userId: RequestBody, postId: RequestBody, images: List<MultipartBody.Part>, content: RequestBody?) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.editPost(userId, postId, images, content)
            }.onSuccess {
                _msg.postValue(it.message)
            }.onFailure { e ->
                Log.e("ErLOg", "Error editing post", e)
            }
        }
    }

    fun likePost(userId: String, postId: String, like: Boolean, authorData: AuthorData) {
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
            }.onSuccess {
                _posts.postValue(_posts.value?.map { post ->
                    if (post._id == postId) {
                        val updatedLikes = if (like) {
                            post.listLike + authorData
                        } else {
                            post.listLike.filter { it.username != authorData.username }
                        }
                        val updatedTotalLike = if (like) {
                            post.totalLike + 1
                        } else {
                            post.totalLike - 1
                        }
                        post.copy(listLike = updatedLikes, totalLike = updatedTotalLike)
                    } else {
                        post
                    }
                })
                _userPosts.postValue(_userPosts.value?.map { post ->
                    if (post._id == postId) {
                        val updatedLikes = if (like) {
                            post.listLike + authorData
                        } else {
                            post.listLike.filter { it.username != authorData.username }
                        }
                        val updatedTotalLike = if (like) {
                            post.totalLike + 1
                        } else {
                            post.totalLike - 1
                        }
                        post.copy(listLike = updatedLikes, totalLike = updatedTotalLike)
                    } else {
                        post
                    }
                })
            }

        }
    }

    fun deletePost(userId: String, postId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                postRepository.deletePost(DeletePostRequest(userId, postId))
            }.onSuccess {
                _msg.postValue(it.message)
            }
        }
    }

}