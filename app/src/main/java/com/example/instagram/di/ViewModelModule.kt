package com.example.instagram.di

import com.example.instagram.viewmodels.AuthViewModel
import com.example.instagram.viewmodels.PostViewModel
import com.example.instagram.viewmodels.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { AuthViewModel(authRepository = get()) }
    viewModel { PostViewModel(postRepository = get()) }
    viewModel { UserViewModel(userRepository = get()) }
}