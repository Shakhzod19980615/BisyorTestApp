package com.example.testapp.data.remote.dto.chat

import com.example.testapp.domain.model.chat.CreateChatModel
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName

data class CreateChatResponse(
    val status: String?,
    val chat_id: Int?,
    val message: String?
)

fun CreateChatResponse.toCreateChatModel() = CreateChatModel(
    status = status.orEmpty(),
    chatId = chat_id ?: -1,
    message = message.orEmpty()
)
