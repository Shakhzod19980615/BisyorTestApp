package com.example.testapp.domain.model.chat

data class ChatSortedByDateModel (
    val date: String,
    val messages: List<Message>
)