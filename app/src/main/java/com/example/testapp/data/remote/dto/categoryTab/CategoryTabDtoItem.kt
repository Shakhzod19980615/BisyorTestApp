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
    hasChild = addr==1,
    categoryId = categoryId,
    icon = icon,
    keyword = keyword,
    title = title
)