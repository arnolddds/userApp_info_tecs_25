package com.sobolev.userapp_infotecs_25.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.sobolev.userapp_infotecs_25.data.local.models.LocationDbModel
import com.sobolev.userapp_infotecs_25.data.local.models.LoginDbModel
import com.sobolev.userapp_infotecs_25.data.local.models.NameDbModel
import com.sobolev.userapp_infotecs_25.data.local.models.PictureDbModel
import com.sobolev.userapp_infotecs_25.data.local.models.RegistrationDateDbModel
import com.sobolev.userapp_infotecs_25.data.local.models.StreetDbModel
import com.sobolev.userapp_infotecs_25.data.local.models.UserDbModel
import com.sobolev.userapp_infotecs_25.data.network.dto.LocationDto
import com.sobolev.userapp_infotecs_25.data.network.dto.LoginDto
import com.sobolev.userapp_infotecs_25.data.network.dto.NameDto
import com.sobolev.userapp_infotecs_25.data.network.dto.PictureDto
import com.sobolev.userapp_infotecs_25.data.network.dto.RegistrationDateDto
import com.sobolev.userapp_infotecs_25.data.network.dto.StreetDto
import com.sobolev.userapp_infotecs_25.data.network.dto.UserDto
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun UserDto.toDbModel(): UserDbModel {
    return UserDbModel(
        id = email.hashCode(),
        gender = gender,
        name = name.toDbModel(),
        location = location.toDbModel(),
        email = email,
        phone = phone,
        cell = cell,
        picture = picture.toDbModel(),
        nat = nationality,
        dob = dob?.age.toString(),
        login = login.toDbModel(),
        registered = registered.toDbModel()
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun RegistrationDateDto.toDbModel(): RegistrationDateDbModel {
    return RegistrationDateDbModel(
        date = formatDate(date),
        age = age
    )
}


fun LoginDto.toDbModel(): LoginDbModel {
    return LoginDbModel(
        username = username
    )
}
fun NameDto.toDbModel(): NameDbModel {
    return NameDbModel(
        title = title,
        first = first,
        last = last
    )
}


fun LocationDto.toDbModel(): LocationDbModel {
    return LocationDbModel(
        street = street.toDbModel(),
        city = city,
        state = state,
        country = country,
        postcode = postcode
    )
}



fun StreetDto.toDbModel(): StreetDbModel {
    return StreetDbModel(
        number = number,
        name = name
    )
}


fun PictureDto.toDbModel(): PictureDbModel {
    return PictureDbModel(
        large = large,
        medium = medium,
        thumbnail = thumbnail
    )
}

@RequiresApi(Build.VERSION_CODES.O)
private fun formatDate(dateString: String): String {
    val inputFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME
    val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    val parsedDate = ZonedDateTime.parse(dateString, inputFormatter)
    return parsedDate.format(outputFormatter)
}



