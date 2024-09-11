package com.example.testapp.presentation.home.viewModel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel
import com.example.testapp.domain.use_case.announcement.GetAnnouncementListUseCase
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
    private val errorParser: ErrorParser
): ViewModel() {
    private val _announcementItems = MutableStateFlow<Resource<List<AnnouncementItemModel>>>(Resource.Loading())
    val announcementItems: StateFlow<Resource<List<AnnouncementItemModel>>> get()
    = _announcementItems.asStateFlow()

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getAnnouncementList(categoryId: Int){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                kotlin.runCatching {//handle exceptions
                    getAnnouncementListUseCase.invoke(categoryId)
                }.onSuccess {
                    _announcementItems.value = Resource.Success(it)
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
            try {
                val announcementItems = getAnnouncementListUseCase.invoke(categoryId)
                _announcementItems.value = Resource.Success(announcementItems)
            } catch (e: HttpException) {
                (Resource.Error( "An unexpected error occured", null))

            } catch (e: IOException) {
                (Resource.Error("Couldn't reach server. Check your internet connection.", null))
            }
        }
    }
}