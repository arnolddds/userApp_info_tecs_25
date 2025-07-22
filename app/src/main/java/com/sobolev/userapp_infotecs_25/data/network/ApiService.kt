package com.sobolev.userapp_infotecs_25.data.network

import com.sobolev.userapp_infotecs_25.data.network.dto.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/")
    suspend fun getAllUsers(
        @Query("results") count: Int = 10
    ): UserResponse
}