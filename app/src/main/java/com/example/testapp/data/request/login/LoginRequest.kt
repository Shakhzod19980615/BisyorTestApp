package com.example.testapp.data.request.login
import kotlinx.serialization.SerialName

data class LoginRequest(
    @SerialName("lang")
    val lang: String,
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String
)
