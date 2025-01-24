package com.example.testapp.data.request.chat

import kotlinx.serialization.SerialName

data class ChatRequest(
    @SerialName("lang")
    val lang: String,
    @SerialName("id")
    val id: Int
)

data class ChatByAnnouncementRequest(
    @SerialName("lang")
    val lang: String,
    @SerialName("page")
    val offset: Int,
    @SerialName("id")
    val id: Int
)

data class CreateChatRequest(
    @SerialName("lang")
    val lang: String,
    @SerialName("user_id")
    val userId: Int,
    @SerialName("item_id")
    val itemId: Int
)