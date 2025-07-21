package com.sobolev.userapp_infotecs_25.domain.entities

import com.sobolev.usersapp.domain.entities.Street

data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String
)