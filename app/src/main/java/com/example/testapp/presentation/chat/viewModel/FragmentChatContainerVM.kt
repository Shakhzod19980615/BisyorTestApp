package com.example.testapp.presentation.chat.viewModel

import ChatModel
import UserChat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.chat.ChatRequest
import com.example.testapp.domain.use_case.chat.UserMainChatUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class FragmentChatContainerVM @Inject constructor(
    private val userMainChatUseCase: UserMainChatUseCase,
    private val errorParser: ErrorParser
):ViewModel() {

    private val _allChats = MutableStateFlow<Resource<List<UserChat>>>(Resource.Loading())
    val allChats : StateFlow<Resource<List<UserChat>>> = _allChats

    fun getChats(param:Pair<Int, ChatRequest>){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {
                    userMainChatUseCase.invoke(param)
                }.onSuccess {
                  _allChats.value = Resource.Success(it)
                }.onFailure {
                        throwable ->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _allChats.value = Resource.Error(parsedError.message)
                                } else {
                                    _allChats.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _allChats.value = Resource.Error("Unknown error")
                            }
                        }
                    }
                }
            }
        }
    }
}