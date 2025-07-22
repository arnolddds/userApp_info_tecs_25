package com.sobolev.userapp_infotecs_25.data.network.dto

import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("gender") val gender: String,
    @SerializedName("name") val name: NameDto,
    @SerializedName("location") val location: LocationDto,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("cell") val cell: String,
    @SerializedName("picture") val picture: PictureDto,
    @SerializedName("nat") val nationality: String,
    @SerializedName("dob") val dob: DateOfBirthDto? = null,
    @SerializedName("registered") val registered: RegistrationDateDto,
    @SerializedName("login") val login: LoginDto
)