package com.example.testapp.domain.repository.searchRepository

import com.example.testapp.data.remote.dto.searchCategory.CategoryResponseDto

interface SearchRepository {
    suspend fun getActiveSubCategories(categoryId: Int,lang:String): List<CategoryResponseDto>
}