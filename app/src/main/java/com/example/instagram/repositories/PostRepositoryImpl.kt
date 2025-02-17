package com.example.instagram.repositories

import android.content.Context
import android.util.Log
import com.example.instagram.data.enums.Sort
import com.example.instagram.data.models.request.DeletePostRequest
import com.example.instagram.data.models.request.LikePostRequest
import com.example.instagram.data.models.response.AllPostsResponse
import com.example.instagram.data.models.response.ApiResponse
import com.example.instagram.data.models.response.PostResponse
import com.example.instagram.retrofit.PostApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PostRepositoryImpl(private val context: Context) : PostRepository {
    private val postApiService = PostApiService.postService

    override suspend fun getAllPosts(
        sort: String,
        page: Int,
        perPage: Int
    ): ApiResponse<AllPostsResponse> {
        return postApiService.getAllPosts(sort, page, perPage)
    }

    override suspend fun getUserPosts(
        username: String,
        sort: String,
        page: Int,
        perPage: Int
    ): ApiResponse<AllPostsResponse> {
        return postApiService.getUserPosts(username, sort, page, perPage)
    }

    override suspend fun getAllUserPosts(username: String): ApiResponse<AllPostsResponse> {
        return postApiService.getAllUserPosts(username)
    }

    override suspend fun addPost(
        userId: RequestBody,
        images: List<MultipartBody.Part>,
        content: RequestBody?
    ): ApiResponse<PostResponse> {
        return postApiService.addPost(userId, images, content)
    }


    override suspend fun likePost(likePostRequest: LikePostRequest): ApiResponse<Any> {
        return postApiService.likePost(likePostRequest)
    }

    override suspend fun editPost(
        userId: RequestBody,
        postId: RequestBody,
        images: List<MultipartBody.Part>,
        content: RequestBody?
    ): ApiResponse<PostResponse> {
        return postApiService.editPost(userId, postId, images, content)
    }

    override suspend fun deletePost(deletePostRequest: DeletePostRequest): ApiResponse<Any> {
        return postApiService.deletePost(deletePostRequest)
    }
}