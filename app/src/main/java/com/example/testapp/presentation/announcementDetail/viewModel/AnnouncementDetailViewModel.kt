package com.example.testapp.presentation.announcementDetail.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.chat.CreateChatRequest
import com.example.testapp.domain.model.announcementItemDetails.AnnouncementItemDetailsModel
import com.example.testapp.domain.model.chat.CreateChatModel
import com.example.testapp.domain.use_case.announcementDetails.GetAnnouncementDetailsUseCase
import com.example.testapp.domain.use_case.chat.CreateChatUseCase
import com.example.testapp.domain.use_case.chat.UserMainChatUseCase
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class AnnouncementDetailViewModel @Inject constructor(
    private val getAnnouncementDetailsUseCase: GetAnnouncementDetailsUseCase,
    private val errorParser: ErrorParser,
    private val createChatUseCase: UserMainChatUseCase
): ViewModel() {
    private val _announcementDetail = MutableStateFlow<Resource<AnnouncementItemDetailsModel>>(Resource.Loading())
    val announcementDetail: StateFlow<Resource<AnnouncementItemDetailsModel>> = _announcementDetail.asStateFlow()

    private val _isExpandedText = MutableStateFlow(true) // Initial state
    val isExpandedText: StateFlow<Boolean> get() = _isExpandedText.asStateFlow()

    private val _createChatModel = MutableStateFlow<Resource<CreateChatModel>>(Resource.Loading())
     val createChatModel: StateFlow<Resource<CreateChatModel>> = _createChatModel.asStateFlow()
    fun setActiveSegment(isActive: Boolean) {
        viewModelScope.launch {
            _isExpandedText.emit(isActive)
        }
    }
    fun getAnnouncementDetails(itemId: Int?){
        viewModelScope.launch {
            try {
                val announcementDetail = getAnnouncementDetailsUseCase.invoke(itemId = itemId?:-1)
                _announcementDetail.value = Resource.Success(announcementDetail)
            } catch (e: HttpException) {
                (Resource.Error( "An unexpected error occured", null))
            }catch (e: IOException) {
                (Resource.Error("Couldn't reach server. Check your internet connection.", null))
            }
        }
    }

    fun createChat(lang:String, userId:Int,itemId:Int) {
        val body = CreateChatRequest(lang = lang, userId = userId, itemId = itemId)
        Log.d("CreateChat", "Request Body: $body")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    createChatUseCase.createChat(body)
                }.onSuccess {
                    _createChatModel.value = Resource.Success(it)
                }.onFailure { throwable ->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _createChatModel.value = Resource.Error(parsedError.message)
                                } else {
                                    _createChatModel.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _createChatModel.value = Resource.Error("Unknown error")
                            }
                        }
            }
        }
    }
}
    }
}