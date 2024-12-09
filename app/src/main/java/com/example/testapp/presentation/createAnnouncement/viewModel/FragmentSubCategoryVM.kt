package com.example.testapp.presentation.createAnnouncement.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.ErrorParser
import com.example.testapp.common.Resource
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.model.categoryModel.CategoryModel
import com.example.testapp.domain.use_case.searchUseCase.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import javax.inject.Inject
@HiltViewModel
class FragmentSubCategoryVM @Inject constructor(
    private val activeCategoryUseCase: SearchUseCase,
    private val errorParser: ErrorParser
): ViewModel() {
    private val _subCategories = MutableStateFlow<Resource<List<CategoryModel>>>(Resource.Loading())
    val subCategories: StateFlow<Resource<List<CategoryModel>>> get() = _subCategories

    private val _getItemsByCategroy = MutableStateFlow<Resource<List<AnnouncementItemModel>>>(Resource.Loading())
    val getItemsByCategroy: StateFlow<Resource<List<AnnouncementItemModel>>> get() = _getItemsByCategroy

    private val _getItemsByQuery = MutableStateFlow<Resource<List<AnnouncementItemModel>>>(Resource.Loading())
    val getItemsByQuery: StateFlow<Resource<List<AnnouncementItemModel>>> get() = _getItemsByQuery
     fun getSubCategories(categoryId: Int,lang:String) {
        _subCategories.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    activeCategoryUseCase.getActiveCategoryList(categoryId,lang)
                }.onSuccess {
                    _subCategories.value = Resource.Success(it)
                }.onFailure { throwable ->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _subCategories.value = Resource.Error(parsedError.message)
                                } else {
                                    _subCategories.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _subCategories.value = Resource.Error("Unknown error")
                            }
                        }
                    }
                }
            }
        }
    }

    fun getItemsByCategory(categoryId: Int,offset:Int,lang:String) {
        _getItemsByCategroy.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    activeCategoryUseCase.getItemsByCategory(categoryId, offset, lang)
                }.onSuccess {
                    _getItemsByCategroy.value = Resource.Success(it)
                }.onFailure { throwable ->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _getItemsByCategroy.value = Resource.Error(parsedError.message)
                                } else {
                                    _getItemsByCategroy.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _getItemsByCategroy.value = Resource.Error("Unknown error")

                            }
                        }
                    }
                }
            }
        }
    }

    fun getItemsByQuery(query:String,offset:Int,lang:String,sorting:String?) {
        _getItemsByQuery.value = Resource.Loading()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    activeCategoryUseCase.getItemsByQuery(query, offset, lang, sorting)
                }.onSuccess {
                    _getItemsByQuery.value = Resource.Success(it)
                }.onFailure { throwable ->
                    when (throwable) {
                        is retrofit2.HttpException -> {
                            val errorResponse: Response<*>? = throwable.response()
                            if (errorResponse?.errorBody() != null) {
                                val parsedError = errorParser.parseError(errorResponse)
                                if (parsedError != null) {
                                    _getItemsByQuery.value = Resource.Error(parsedError.message)
                                } else {
                                    _getItemsByQuery.value = Resource.Error("Unknown error")
                                }
                            } else {
                                _getItemsByQuery.value = Resource.Error("Unknown error")
                            }
                        }
                    }
                }
            }
        }
    }

}