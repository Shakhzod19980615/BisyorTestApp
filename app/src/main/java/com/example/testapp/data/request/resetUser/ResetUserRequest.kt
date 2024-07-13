package com.example.testapp.data.request.resetUser

import kotlinx.serialization.SerialName

data class ResetUserRequest (
    @SerialName("login")
    val login: String
)