package com.example.testapp.domain.use_case.chat

import com.example.testapp.data.request.chat.CreateChatRequest
import com.example.testapp.domain.model.chat.CreateChatModel
import com.example.testapp.domain.repository.chat.ChatRepository
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val repository: ChatRepository
) {
    suspend fun createChat(param: CreateChatRequest): CreateChatModel {
        return  repository.getAnnouncementChatsById(param)
    }
}