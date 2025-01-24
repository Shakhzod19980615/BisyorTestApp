package com.example.testapp.domain.use_case.chat

import android.util.Log
import com.example.testapp.data.request.chat.SendTextMessageRequest
import com.example.testapp.domain.model.basicResponseModel.SendChatResponseModel
import com.example.testapp.domain.repository.chat.ChatRepository
import javax.inject.Inject

class SendMessageUseCase  @Inject constructor(
    private val repository: ChatRepository
){
    suspend fun sendMessage(body: SendTextMessageRequest): SendChatResponseModel {
        Log.d("SendMessage", "toSendChatResponse: $body")
        return repository.sendMessage(body)
    }
}