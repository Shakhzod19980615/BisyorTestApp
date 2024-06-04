package com.example.testapp.data.request.verificationCode

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class VerificationCodeRequest(
    @SerialName("login")
    val login: String,
    @SerialName("code")
    val code: String
)
