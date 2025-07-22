package com.sobolev.userapp_infotecs_25.data.local.models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val gender: String,
    @Embedded val name: NameDbModel,
    @Embedded val location: LocationDbModel,
    val email: String,
    val phone: String,
    val cell: String,
    @Embedded val picture: PictureDbModel,
    val nat: String,
    val dob: String,
    @Embedded val login: LoginDbModel,
    @Embedded val registered: RegistrationDateDbModel,
)