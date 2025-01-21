package com.example.testapp.domain.model.chat

data class CreateChatModel(
    val status: String,
    val chatId: Int,
    val message: String
)
