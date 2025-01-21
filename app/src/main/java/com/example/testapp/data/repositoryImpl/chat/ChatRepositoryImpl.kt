package com.example.testapp.data.repositoryImpl.chat

import UserChat
import android.util.Log
import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.basicResponse.toSendChatResponseModel
import com.example.testapp.data.remote.dto.chat.CreateChatResponse
import com.example.testapp.data.remote.dto.chat.toCreateChatModel
import com.example.testapp.data.remote.dto.chat.toMessageModel
import com.example.testapp.data.request.chat.ChatByAnnouncementRequest
import com.example.testapp.data.request.chat.ChatRequest
import com.example.testapp.data.request.chat.CreateChatRequest
import com.example.testapp.data.request.chat.MessageRequest
import com.example.testapp.data.request.chat.SendTextMessageRequest
import com.example.testapp.domain.model.basicResponseModel.SendChatResponseModel
import com.example.testapp.domain.model.chat.CreateChatModel
import com.example.testapp.domain.model.chat.LiveUser
import com.example.testapp.domain.model.chat.MessageItems
import com.example.testapp.domain.model.chat.MessageModel
import com.example.testapp.domain.model.chat.Messages
import com.example.testapp.domain.repository.chat.ChatRepository
import com.google.gson.Gson
import retrofit2.HttpException
import toChatModel
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
   private val api:AppService
):ChatRepository {
    override suspend fun getAllChats(param: ChatRequest): List<UserChat> {
        return try {
            val response = api.getAllChats(param.lang, param.id) // Make the API call
            val chatModel = response.toChatModel() // Convert response to ChatModel
            val users = chatModel.users // Extract users list

            // Log the result
            //Log.d("ChatRepositoryImpl", "toChatModel().users: $users")

            users
        }catch (e:Exception){
           // Log.e("ChatRepositoryImpl", "Error fetching chats: ${e.localizedMessage}", e)
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

    /*override suspend fun getAnnouncementChatsById(body: CreateChatRequest): CreateChatModel {
        return try {
            val response = api.createChatByAnnouncement(body = body)
            Log.d("CreateChat", "Raw API Response: $response")
            val mappedModel = response.toCreateChatModel()
            Log.d("CreateChat", "Mapped Model: $mappedModel")
            mappedModel
        } catch (e: Exception) {
            Log.e("CreateChat", "Error in API call", e)
            CreateChatModel(
                status = "Error",
                chatId = -1,
                message = e.message ?: "Unknown error"
            )
        }
    }
*/
    override suspend fun getAnnouncementChatsById(body: CreateChatRequest): CreateChatModel {
        return try {
            val response = api.createChatByAnnouncement(body)
            if (response.status =="success") {
                response.toCreateChatModel()
            } else {
                // Log the error response body
                val errorBody = response.message
                Log.e("CreateChat", "HTTP ${response.status}: ${response.message} | Error Body: $errorBody")
                CreateChatModel(
                    status = "Error",
                    chatId = -1,
                    message = errorBody ?: "Unknown error"
                )
            }
        } catch (e: Exception) {
            Log.e("CreateChat", "Error in Repository", e)
            CreateChatModel(
                status = "Error",
                chatId = -1,
                message = e.message ?: "Unknown error"
            )
        }
    }



    override suspend fun getMessage(param: MessageRequest): MessageModel {
        return try{
            api.getUserMessages(lang = param.lang, id = param.chatId, offset = param.offset)
                .toMessageModel()
        }catch (e:Exception){
            MessageModel(
                chat_id = 0.toString(),
                items = MessageItems(
                    item_id = 0,
                    item_image = "",
                    item_title = ""
                ),
                liveUser = LiveUser(
                    avatar = "",
                    email = "",
                    lastSeen = "",
                    phone = "",
                    statusOnline = false,
                    userFIO = "",
                    userId = 0
                ),
                messages = Messages(
                    hasNextPage = false,
                    itemCount = 0,
                    messagesList = emptyList()
                )
            )
        }
    }

    override suspend fun sendMessage(body: SendTextMessageRequest): SendChatResponseModel {
        return try {
            Log.d("SendMessage", "Sending Request Body: ${Gson().toJson(body)}")
            val response = api.sendMessageToUser(body = body)
            val toSendChatResponse = response.toSendChatResponseModel()
            Log.d("SendMessage", "Response: $toSendChatResponse")
            toSendChatResponse
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = e.response()?.errorBody()?.string()
                Log.e("SendMessageError", "HTTP ${e.code()}, Error Response: $errorResponse")
            } else {
                Log.e("SendMessageError", "Unexpected Error: ${e.message}")
            }
            return SendChatResponseModel(
                chatId = 0,
                status = "error",
                message = "error"
            )
        }
    }

}