package com.sobolev.userapp_infotecs_25.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("results") val results: List<UserDto>
)