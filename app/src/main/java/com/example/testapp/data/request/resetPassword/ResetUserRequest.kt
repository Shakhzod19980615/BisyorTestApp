package com.example.testapp.data.request.resetPassword

import kotlinx.serialization.SerialName

data class ResetUserRequest (
    @SerialName("login")
    val login: String
)