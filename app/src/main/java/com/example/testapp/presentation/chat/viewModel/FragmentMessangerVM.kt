package com.example.testapp.presentation.chat.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.chat.MessageRequest
import com.example.testapp.data.request.chat.SendTextMessageRequest
import com.example.testapp.domain.model.basicResponseModel.SendChatResponseModel
import com.example.testapp.domain.model.chat.MessageModel
import com.example.testapp.domain.use_case.chat.GetMessageUseCase
import com.example.testapp.domain.use_case.chat.SendMessageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FragmentMessangerVM @Inject constructor(
    private val errorParser: ErrorParser,
    private val getMessageUseCase: GetMessageUseCase,
    private val sendMessageUseCase: SendMessageUseCase
): ViewModel() {
     private val _message = MutableStateFlow<Resource<MessageModel>>(Resource.Loading())
     val message: StateFlow<Resource<MessageModel>> = _message

    private val _sendMessage = MutableStateFlow<Resource<SendChatResponseModel>>(Resource.Loading())
    val sendMessage: StateFlow<Resource<SendChatResponseModel>> = _sendMessage

    fun getMessage(param:MessageRequest) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    getMessageUseCase.getMessage(param)
                }.onSuccess {
                    _message.value = Resource.Success(it)
                }.onFailure { throwable ->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _message.value = Resource.Error(parsedError.message)
                                } else {
                                    _message.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _message.value = Resource.Error("Unknown error")
                            }
                        }

                    }
                }
            }
        }
    }


    fun sendMessage(lang:String, chatId:Int, text:String) {
        val body = SendTextMessageRequest(lang = lang, chatId = chatId, message = text)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    Log.d("SendMessage", "toSendChatResponse: $body")
                    sendMessageUseCase.sendMessage(body)
                }.onSuccess {
                    _sendMessage.value = Resource.Success(it)
                    val param = MessageRequest(lang = "ru", chatId = it.chatId, offset = 0)
                    getMessage(param)
                }.onFailure { throwable ->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _message.value = Resource.Error(parsedError.message)
                                } else {
                                    _message.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _message.value = Resource.Error("Unknown error")
                            }
                        }
                    }
                }
            }
        }
    }
}