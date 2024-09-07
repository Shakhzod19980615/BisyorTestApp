package com.example.testapp.domain.repository.searchRepository

import com.example.testapp.data.remote.dto.searchCategory.CategoryResponseDto
import com.example.testapp.domain.model.categoryModel.CategoryModel

interface SearchRepository {
    suspend fun getActiveSubCategories(categoryId: Int,lang:String): List<CategoryModel>
}