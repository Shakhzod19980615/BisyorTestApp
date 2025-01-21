package com.example.testapp.data.remote.dto.chat

import Item
import com.example.testapp.domain.model.chat.LiveUser
import com.example.testapp.domain.model.chat.Message
import com.example.testapp.domain.model.chat.MessageItems
import com.example.testapp.domain.model.chat.MessageModel
import com.example.testapp.domain.model.chat.Messages

data class MessageResponseDto(
    val chat_id: String?,
    val items: ItemsResponse,
    val liveUser: LiveUserResponse,
    val messages: MessagesResponse
)
data class ItemsResponse(
    val item_id: Int?,
    val item_image: String?,
    val item_title: String?
)
data class LiveUserResponse(
    val avatar: String?,
    val email: String?,
    val lastSeen: String?,
    val phone: String?,
    val statusOnline: Boolean,
    val userFIO: String?,
    val userId: Int?
)
data class MessagesResponse(
    val hasNextPage: Boolean,
    val itemCount: Int?,
    val messagesList: List<MessageResponse>
)
data class MessageResponse(
    val creator: String?,
    val date_cr: String?,
    val is_read: Boolean,
    val message: String?,
    val messageId: Int?,
    val type: String?
)
fun MessageResponseDto.toMessageModel() = MessageModel(
    chat_id = chat_id?:"0",
    items = items.toItem(),
    liveUser = liveUser.toLiveUser(),
    messages = messages.toMessageList()
)
fun ItemsResponse.toItem() = MessageItems(
    item_id = item_id?:0,
    item_image = item_image?:"",
    item_title = item_title?:""
)
fun LiveUserResponse.toLiveUser() = LiveUser(
    avatar = avatar?:"",
    email = email?:"",
    lastSeen = lastSeen?:"",
    phone = phone?:"",
    statusOnline = statusOnline,
    userFIO = userFIO?:"",
    userId = userId?:0
)
fun MessagesResponse.toMessageList() = Messages(
    hasNextPage = hasNextPage,
    itemCount = itemCount?:0,
    messagesList = messagesList.map { it.toMessage() }
)
fun MessageResponse.toMessage() = Message(
    creator = creator?:"",
    date_cr = date_cr?:"",
    is_read = is_read?:false,
    message = message?:"",
    messageId = messageId?:0,
    type = type?:""
)