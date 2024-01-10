package com.example.testapp.domain.repository

import com.example.testapp.data.remote.dto.CategoryDto
import com.example.testapp.data.remote.dto.CategoryDtoItem

interface CategoryRepository {

     suspend fun getAllMainCategories(): List<CategoryDtoItem>
}