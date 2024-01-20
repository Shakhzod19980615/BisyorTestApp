package com.example.testapp.data.remote.dto.announcementItemDetails

import com.google.gson.annotations.SerializedName

data class User(
    val apiKey: Any,
    val avatar: String,
    @SerializedName("is_verify_user")
    val is_verify_user: Any,
    val name: String,
    val userId: Int,
    val userPhone: List<String>
)