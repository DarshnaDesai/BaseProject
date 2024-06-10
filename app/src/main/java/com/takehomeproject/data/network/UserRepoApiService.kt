package com.takehomeproject.data.network

import com.takehomeproject.data.model.UserRepoResponse
import com.takehomeproject.data.model.UserResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface UserRepoApiService {

    @GET("users/{user}")
    suspend fun getUser(@Path("user") userId: String): UserResponse

    @GET("users/{user}/repos")
    suspend fun getUserRepoList(@Path("user") userId: String): List<UserRepoResponse>
}