package com.example.testapp.data.request.chat

import kotlinx.serialization.SerialName

data class MessageRequest(
    @SerialName("lang")
    val lang: String,
    @SerialName("chat_id")
    val chatId: Int,
    @SerialName("page")
    val offset: Int
)