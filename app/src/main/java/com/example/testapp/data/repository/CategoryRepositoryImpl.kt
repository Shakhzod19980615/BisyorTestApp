package com.example.testapp.data.repository

import com.example.testapp.data.remote.AppService
import com.example.testapp.data.remote.dto.CategoryDto
import com.example.testapp.data.remote.dto.CategoryDtoItem
import com.example.testapp.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val api : AppService
):CategoryRepository {
    override suspend fun getAllMainCategories(): List<CategoryDtoItem> {
       return api.getAllMainCategories()
    }
}