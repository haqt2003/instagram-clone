package com.example.instagram.data.models.request

import java.io.File

data class UpdatePostRequest(val userId: String, val image: List<File>, val content: String, val postId: String)
