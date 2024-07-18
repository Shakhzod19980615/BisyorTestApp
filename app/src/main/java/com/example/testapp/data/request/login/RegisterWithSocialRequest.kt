package com.example.testapp.data.request.login

import kotlinx.serialization.SerialName

data class RegisterWithSocialRequest (
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: String,
    @SerialName("username")
    val name: String? = null,
    @SerialName("email")
    val email: String?=null,
    @SerialName("phone")
    val phone: String?=null
)