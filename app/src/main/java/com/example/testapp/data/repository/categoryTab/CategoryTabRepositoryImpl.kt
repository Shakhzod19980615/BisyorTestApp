package com.example.testapp.data.repository.categoryTab

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.categoryTab.CategoryDtoItem
import com.example.testapp.domain.repository.categoryTab.CategoryTabRepository
import javax.inject.Inject

class CategoryTabRepositoryImpl @Inject constructor(
    private val api : AppService
): CategoryTabRepository {
    override suspend fun getAllMainCategories(): List<CategoryDtoItem> {
       return api.getAllMainCategories()
    }
}