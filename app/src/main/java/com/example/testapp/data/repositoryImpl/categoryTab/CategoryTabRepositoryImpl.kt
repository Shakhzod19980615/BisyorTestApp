package com.example.testapp.data.repositoryImpl.categoryTab

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.categoryTab.CategoryDtoItem
import com.example.testapp.data.remote.dto.categoryTab.toCategoryTabItem
import com.example.testapp.domain.model.categoryModel.CategoryModel
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel
import com.example.testapp.domain.repository.categoryTab.CategoryTabRepository
import javax.inject.Inject

class CategoryTabRepositoryImpl @Inject constructor(
    private val api : AppService
): CategoryTabRepository {
    override suspend fun getAllMainCategories(): List<CategoryModel> {
       return try {
           api.getAllMainCategories().map { it.toCategoryTabItem() }
       } catch (e:Exception){
           emptyList()
       }
    }
}