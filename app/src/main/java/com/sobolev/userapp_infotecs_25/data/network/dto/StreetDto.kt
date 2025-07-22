package com.sobolev.userapp_infotecs_25.data.network.dto

import com.google.gson.annotations.SerializedName

data class StreetDto(
    @SerializedName("number") val number: Int,
    @SerializedName("name") val name: String
)