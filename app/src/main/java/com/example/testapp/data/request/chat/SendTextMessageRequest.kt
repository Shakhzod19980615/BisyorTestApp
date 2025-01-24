package com.example.testapp.data.request.chat

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName


data class SendTextMessageRequest (
    @SerializedName("lang")
    val lang: String,
    @SerializedName("chat_id")
    val chatId: Int,
    @SerializedName("message")
    val message: String
)