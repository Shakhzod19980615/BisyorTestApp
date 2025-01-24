package com.example.testapp.domain.use_case.chat

import ChatModel
import UserChat
import com.example.testapp.data.request.chat.ChatRequest
import com.example.testapp.data.request.chat.CreateChatRequest
import com.example.testapp.domain.model.chat.CreateChatModel
import com.example.testapp.domain.repository.chat.ChatRepository
import javax.inject.Inject

class UserMainChatUseCase @Inject constructor(
    private val repository: ChatRepository
){
    suspend fun invoke(param:Pair<Int,ChatRequest >):List<UserChat>{
        return when(param.first){
            1-> repository.getAllChats(param.second)
            2-> repository.getAdminChats(param.second)
            else-> repository.getAllAnnouncementChats(param.second)
        }
    }
    suspend fun createChat(param: CreateChatRequest): CreateChatModel {
        return  repository.getAnnouncementChatsById(param)
    }
}