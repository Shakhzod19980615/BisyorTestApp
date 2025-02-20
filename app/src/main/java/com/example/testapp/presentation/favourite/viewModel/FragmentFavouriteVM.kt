package com.example.testapp.presentation.favourite.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.favourite.UserSubscriptionsRequest
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.model.userDataModel.SubscribedUserModel
import com.example.testapp.domain.use_case.chat.UserMainChatUseCase
import com.example.testapp.domain.use_case.favourite.ChangeFavouriteUseCase
import com.example.testapp.domain.use_case.favourite.UserRemoteFavoritesUseCase
import com.example.testapp.domain.use_case.subscribtion.GetSubscriptionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
@HiltViewModel
class FragmentFavouriteVM @Inject constructor(
    private val useCase: UserRemoteFavoritesUseCase,
    private val errorParser: ErrorParser,
    private val getSubscriptionUseCase: GetSubscriptionUseCase,
    private val changeFavouriteUseCase: ChangeFavouriteUseCase
): ViewModel() {
    private val _favouriteItems = MutableStateFlow<Resource<List<AnnouncementItemModel>>>(Resource.Loading())
    val favouriteItems: StateFlow<Resource<List<AnnouncementItemModel>>>
        get()
    = _favouriteItems.asStateFlow()

    private val _subscribedUsers = MutableStateFlow<Resource<List<SubscribedUserModel>>>(Resource.Loading())
    val subscribedUsers: StateFlow<Resource<List<SubscribedUserModel>>>
        get() = _subscribedUsers.asStateFlow()


     fun getFavouriteItems(lang:String,page:Int){
       viewModelScope.launch {
           withContext(Dispatchers.IO){
               kotlin.runCatching {
                   useCase.getFavouriteItems(lang,page)
               }.onSuccess {
                   _favouriteItems.value = Resource.Success(it)
               }.onFailure { throwable->
                   when (throwable) {
                       is retrofit2.HttpException -> {
                           val errorResponse: Response<*>? = throwable.response()
                           if (errorResponse?.errorBody() != null) {
                               val parsedError = errorParser.parseError(errorResponse)
                               if (parsedError != null) {
                                   _favouriteItems.value = Resource.Error(parsedError.message)
                               } else {
                                   _favouriteItems.value = Resource.Error("Unknown error")
                               }
                           } else {
                               _favouriteItems.value = Resource.Error("Unknown error")
                           }
                       }
                   } }
           }
       }
    }
    fun isFavourite(itemId:Int):StateFlow<Boolean>{
        return favouriteItems.map { items->
            when(items){
                is Resource.Success -> items.data.any { it.id == itemId } ?: false
                else -> false
            }
        }.stateIn(viewModelScope, SharingStarted.Lazily, false)
    }
    fun getSubscribedUsers(lang:String,page:Int){
        val param = UserSubscriptionsRequest(lang,page)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    getSubscriptionUseCase.getSubscribedUsers(param)
                }.onSuccess {
                    _subscribedUsers.value = Resource.Success(it)
                }.onFailure { throwable ->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _subscribedUsers.value = Resource.Error(parsedError.message)
                                } else {
                                    _subscribedUsers.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _subscribedUsers.value = Resource.Error("Unknown error")
                            }
                        }
                    }
                }
            }
        }
    }
}
