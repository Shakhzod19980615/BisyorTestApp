package com.example.testapp.data.request.chat

import com.google.gson.annotations.SerializedName
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
    @SerializedName("lang")
    val lang: String,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("item_id")
    val itemId: Int
)