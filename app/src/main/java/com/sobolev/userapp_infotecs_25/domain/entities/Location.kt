package com.sobolev.userapp_infotecs_25.domain.entities


data class Location(
    val street: Street,
    val city: String,
    val state: String,
    val country: String,
    val postcode: String
)