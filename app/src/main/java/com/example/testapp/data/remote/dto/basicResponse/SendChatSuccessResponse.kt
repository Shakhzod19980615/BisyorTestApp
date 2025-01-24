package com.example.testapp.data.remote.dto.basicResponse

import com.example.testapp.domain.model.basicResponseModel.SendChatResponseModel
import com.google.gson.annotations.SerializedName

data class SendChatSuccessResponse(
    @SerializedName("chat_id")
    val chatId: Int,
    val status: String,
    val message: String,

    )

fun SendChatSuccessResponse.toSendChatResponseModel() = SendChatResponseModel(
    chatId = chatId,
    status = status,
    message = message
)