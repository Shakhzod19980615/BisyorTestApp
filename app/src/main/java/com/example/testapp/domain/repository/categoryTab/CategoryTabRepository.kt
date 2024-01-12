package com.example.testapp.domain.repository.categoryTab

import com.example.testapp.data.remote.dto.categoryTab.CategoryDtoItem

interface CategoryTabRepository {

     suspend fun getAllMainCategories(): List<CategoryDtoItem>
}