package com.example.testapp.data.remote.dto.announcementItemDetails

data class User(
    val apiKey: Any,
    val avatar: String,
    val is_verify_user: Any,
    val name: String,
    val userId: Int,
    val userPhone: List<String>
)