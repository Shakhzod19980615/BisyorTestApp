package com.example.testapp.data.repositoryImpl.chat

import ChatModel
import UserChat
import com.example.testapp.data.remote.AppService
import com.example.testapp.data.request.chat.ChatRequest
import com.example.testapp.domain.repository.chat.ChatRepository
import toChatModel
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
   private val api:AppService
):ChatRepository {
    override suspend fun getAllChats(param: ChatRequest): List<UserChat> {
        return try {
            api.getAllChats(param.lang, param.id).toChatModel().users
        }catch (e:Exception){
            emptyList()
        }
    }

    override suspend fun getAdminChats(param: ChatRequest): List<UserChat> {
        return try {
            api.getStoreChats(param.lang, param.id).toChatModel().users
        }catch (e:Exception){
            emptyList()
        }
    }

    override suspend fun getAllAnnouncementChats(param: ChatRequest): List<UserChat> {
        return try {
            api.getAllAnnouncementChats(param.lang, param.id).toChatModel().users
        }catch (e:Exception){
            emptyList()}
    }
}