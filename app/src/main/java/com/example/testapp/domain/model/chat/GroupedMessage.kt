package com.example.testapp.domain.model.chat

sealed class GroupedMessage {
    data class DateSeparator(val date: String) : GroupedMessage()
    data class ChatMessage(val message: Message) : GroupedMessage()

}
