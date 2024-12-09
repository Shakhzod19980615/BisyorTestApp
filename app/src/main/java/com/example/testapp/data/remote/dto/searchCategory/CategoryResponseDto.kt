package com.example.testapp.data.remote.dto.searchCategory

import com.example.testapp.domain.model.categoryModel.CategoryModel

data class CategoryResponseDto (
    val storeCategoryId: Int? = null,
    val title: String = "",
    val categoryId: Int = 0,
    val itemsCount: Int = 0,
    val addr: Int? = null,

)

fun CategoryResponseDto.toCategoryModel() = CategoryModel(
    id = categoryId,
    categoryId = categoryId,
    title = title,
    itemsCount = itemsCount?:0,
    hasChild = addr == 1,
    storeCategoryId = storeCategoryId
)