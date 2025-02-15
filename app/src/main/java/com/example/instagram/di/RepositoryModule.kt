package com.example.instagram.di

import com.example.instagram.repositories.AuthRepository
import com.example.instagram.repositories.AuthRepositoryImpl
import com.example.instagram.repositories.PostRepository
import com.example.instagram.repositories.PostRepositoryImpl
import com.example.instagram.repositories.UserRepository
import com.example.instagram.repositories.UserRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    factory<AuthRepository> { AuthRepositoryImpl(androidContext())}
    factory<PostRepository> { PostRepositoryImpl(androidContext())}
    factory<UserRepository> { UserRepositoryImpl(androidContext())}
}