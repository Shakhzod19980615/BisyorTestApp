package com.example.testapp.domain.model.chat



data class MessageModel(
    val chat_id: String,
    val items: MessageItems,
    val liveUser: LiveUser,
    val messages: Messages
)
data class MessageItems(
    val item_id: Int,
    val item_image: String,
    val item_title: String
)
data class LiveUser(
    val avatar: String,
    val email: String,
    val lastSeen: String,
    val phone: String,
    val statusOnline: Boolean,
    val userFIO: String,
    val userId: Int
)
data class Messages(
    val hasNextPage: Boolean,
    val itemCount: Int,
    val messagesList: List<Message>
)
data class Message(
    val creator: String,
    val date_cr: String,
    val is_read: Boolean,
    val message: String,
    val messageId: Int,
    val type: String
)
