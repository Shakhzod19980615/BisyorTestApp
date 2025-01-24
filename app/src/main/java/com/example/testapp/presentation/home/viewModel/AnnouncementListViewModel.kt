package com.example.testapp.presentation.home.viewModel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.data.request.favourite.ChangeFavoriteStatusRequest
import com.example.testapp.domain.model.ChangeFavouriteModel
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel
import com.example.testapp.domain.use_case.announcement.GetAnnouncementListUseCase
import com.example.testapp.domain.use_case.favourite.ChangeFavouriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


@HiltViewModel
class AnnouncementListViewModel @Inject constructor(
    private val getAnnouncementListUseCase: GetAnnouncementListUseCase,
    private val errorParser: ErrorParser,
    private val changeFavouriteUseCase: ChangeFavouriteUseCase
): ViewModel() {
    private val _announcementItems = MutableStateFlow<Resource<List<AnnouncementItemModel>>>(Resource.Loading())
    val announcementItems: StateFlow<Resource<List<AnnouncementItemModel>>> get()
    = _announcementItems.asStateFlow()

    private val _favouriteStatus = MutableStateFlow<Resource<ChangeFavouriteModel>>(Resource.Loading())
    private val _currentFavourites = MutableStateFlow<List<Int>>(emptyList())
    val currentFavourites: StateFlow<List<Int>> get()
    = _currentFavourites.asStateFlow()

    init {
      fetchFavouriteList()
    }
    fun getAnnouncementList(categoryId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {//handle exceptions
                    getAnnouncementListUseCase.invoke(categoryId)
                }.onSuccess {items ->

                    _announcementItems.value = Resource.Success(items)

                }.onFailure {throwable->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _announcementItems.value = Resource.Error(parsedError.message)
                                } else {
                                    _announcementItems.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _announcementItems.value = Resource.Error("Unknown error")
                            }
                        }
                    }

                }
            }
        }
    }

    fun changeFavouriteStatus(lang: String, itemId: Int) {
        val request = ChangeFavoriteStatusRequest(lang, itemId)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    changeFavouriteUseCase.invoke(request)
                }.onSuccess {
                    _favouriteStatus.value = Resource.Success(it)
                    fetchFavouriteList()
                }.onFailure { throwable ->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _favouriteStatus.value = Resource.Error(parsedError.message)
                                } else {
                                    _favouriteStatus.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _favouriteStatus.value = Resource.Error("Unknown error")
                            }
                        }
                    }
                }
            }
        }
    }
    private fun fetchFavouriteList() {
        // Simulate fetching the favorite list
        viewModelScope.launch {
            kotlin.runCatching {
                changeFavouriteUseCase.getUserFavoriteIds()
            }.onSuccess { favouriteIds ->
                _currentFavourites.value = favouriteIds
                //_favouriteStatus.value = Resource.Success(favouriteIds)

            }.onFailure { throwable ->
                _currentFavourites.value = emptyList()
            }
        }
    }
}