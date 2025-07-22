package com.sobolev.userapp_infotecs_25.data.network.dto

import com.google.gson.annotations.SerializedName

data class DateOfBirthDto(
    @SerializedName("date") val date: String,
    @SerializedName("age") val age: Int
)
