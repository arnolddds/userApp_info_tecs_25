package com.sobolev.userapp_infotecs_25.domain.entities


data class User(
    val id: Int,
    val gender: String,
    val name: Name,
    val location: Location,
    val email: String,
    val phone: String,
    val cell: String,
    val picture: Picture,
    val nat: String,
    val dob: String,
    val login: Login,
    val registered: Registration
) {
    val fullName: String
        get() = "${name.first} ${name.last}"
}