package com.example.testapp.presentation.home.viewModel

import android.net.http.HttpException
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testapp.common.Resource
import com.example.testapp.domain.model.categoryModel.CategoryModel
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel
import com.example.testapp.domain.use_case.categoryTab.GetCategoryTabUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CategoryTabViewModel @Inject constructor(
    private val getCategoriesUseCase: GetCategoryTabUseCase
) : ViewModel() {

    private val _categoryItems = MutableStateFlow<Resource<List<CategoryModel>>>(Resource.Loading())
    val categoryItems: StateFlow<Resource<List<CategoryModel>>> get() = _categoryItems.asStateFlow()


    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    fun getAllCategories() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val category = getCategoriesUseCase.invoke()
                    _categoryItems.value = Resource.Success(category)
                } catch (e: HttpException) {
                    (Resource.Error( "An unexpected error occured", null))

                } catch (e: IOException) {
                    (Resource.Error("Couldn't reach server. Check your internet connection.", null))
                }
            }
        }
    }
}