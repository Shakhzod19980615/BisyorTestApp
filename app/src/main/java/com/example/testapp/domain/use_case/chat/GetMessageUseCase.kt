package com.example.testapp.domain.use_case.chat

import com.example.testapp.data.request.chat.MessageRequest
import com.example.testapp.domain.model.chat.MessageModel
import com.example.testapp.domain.repository.chat.ChatRepository
import javax.inject.Inject

class GetMessageUseCase @Inject constructor(

    private val repository: ChatRepository
) {
    suspend fun getMessage(param: MessageRequest): MessageModel {
        return repository.getMessage(param)
    }
}