package com.example.testapp.domain.repository.chat

import ChatModel
import UserChat
import com.example.testapp.data.remote.dto.chat.MessageResponseDto
import com.example.testapp.data.request.chat.ChatByAnnouncementRequest
import com.example.testapp.data.request.chat.ChatRequest
import com.example.testapp.data.request.chat.CreateChatRequest
import com.example.testapp.data.request.chat.MessageRequest
import com.example.testapp.data.request.chat.SendTextMessageRequest
import com.example.testapp.domain.model.basicResponseModel.SendChatResponseModel
import com.example.testapp.domain.model.chat.CreateChatModel
import com.example.testapp.domain.model.chat.MessageModel

interface ChatRepository {
    suspend fun getAllChats(param:ChatRequest):List<UserChat>
    suspend fun getAdminChats(param:ChatRequest):List<UserChat>
    suspend fun getAllAnnouncementChats(param:ChatRequest):List<UserChat>

    suspend fun getAnnouncementChatsById(body: CreateChatRequest):CreateChatModel

    suspend fun getMessage(param: MessageRequest):MessageModel
    suspend fun sendMessage(body: SendTextMessageRequest):SendChatResponseModel
}