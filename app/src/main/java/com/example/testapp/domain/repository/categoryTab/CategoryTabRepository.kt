package com.example.testapp.domain.repository.categoryTab

import com.example.testapp.data.remote.dto.categoryTab.CategoryDtoItem
import com.example.testapp.domain.model.categoryTab.CategoryTabItemModel

interface CategoryTabRepository {

     suspend fun getAllMainCategories(): List<CategoryTabItemModel>
}