package com.example.testapp.domain.repository.chat

import ChatModel
import UserChat
import com.example.testapp.data.request.chat.ChatRequest

interface ChatRepository {
    suspend fun getAllChats(param:ChatRequest):List<UserChat>
    suspend fun getAdminChats(param:ChatRequest):List<UserChat>
    suspend fun getAllAnnouncementChats(param:ChatRequest):List<UserChat>
}