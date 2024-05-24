package com.example.testapp.data.request

import kotlinx.serialization.SerialName

data class RegistrationRequest(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String

)
