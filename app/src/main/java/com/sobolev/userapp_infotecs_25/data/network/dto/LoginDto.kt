package com.sobolev.userapp_infotecs_25.data.network.dto

import com.google.gson.annotations.SerializedName

data class LoginDto(
    @SerializedName("username") val username: String
)