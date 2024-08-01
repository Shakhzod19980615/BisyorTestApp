package com.example.testapp.data.remote.dto.searchCategory

import com.example.testapp.domain.model.categoryModel.CategoryModel

data class CategoryResponseDto (
    val id: Int = -1,
    val title: String = "",
    val categoryId: Int = 0,
    val itemCount: Int = 0,
    val hasChild : Boolean = false,
    val storeCategoryId: Int? = null
)

fun CategoryResponseDto.toCategoryModel() = CategoryModel(
    id = id,
    title = title,
    categoryId = categoryId,
    itemCount = itemCount,
    hasChild = hasChild,
    storeCategoryId = storeCategoryId
)