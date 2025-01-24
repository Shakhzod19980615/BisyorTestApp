package com.example.testapp.domain.repository.searchRepository

import com.example.testapp.data.remote.dto.searchCategory.CategoryResponseDto
import com.example.testapp.domain.model.announcement.AnnouncementItemModel
import com.example.testapp.domain.model.categoryModel.CategoryModel

interface SearchRepository {
    suspend fun getActiveSubCategories(categoryId: Int,lang:String): List<CategoryModel>
    suspend fun getItemsByCategory(categoryId: Int,offset:Int,lang:String): List<AnnouncementItemModel>

    suspend fun getItemsByQuery(query:String,offset:Int,lang:String, sorting:String?): List<AnnouncementItemModel>
}