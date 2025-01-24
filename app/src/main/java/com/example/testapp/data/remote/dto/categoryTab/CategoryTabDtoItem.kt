package com.example.testapp.data.remote.dto.categoryTab

import com.example.testapp.domain.model.categoryModel.CategoryModel

data class CategoryDtoItem(
    val addr: Int,
    val categoryId: Int,
    val icon: String,
    val keyword: String,
    val title: String
)
fun CategoryDtoItem.toCategoryTabItem() = CategoryModel(
    id = categoryId,
    icon = icon,
    categoryId = categoryId,
    title = title,
    hasChild = addr==1,
)