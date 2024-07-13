package com.example.testapp.data.request.resetPassword

import kotlinx.serialization.SerialName

class ResetUserUpdatePasswordRequest(
    @SerialName ("login")
    val login: String,
    @SerialName ("code")
    val code: String,
    @SerialName ("password")
    val password: String
)