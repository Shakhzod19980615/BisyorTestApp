package com.example.testapp.data.request.resetUserConfirmRequest

import kotlinx.serialization.SerialName

data class ResetUserConfirmRequest(
    @SerialName("login")
    val login: String,
    @SerialName("code")
    val code: String
)
