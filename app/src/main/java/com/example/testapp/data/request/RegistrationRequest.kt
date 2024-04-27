package com.example.testapp.data.request

import kotlinx.serialization.SerialName

data class RegistrationRequest(
    @SerialName("email") val email: String,
    @SerialName("password") val password: String

)
