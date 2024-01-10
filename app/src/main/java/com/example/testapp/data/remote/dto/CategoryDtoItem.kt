package com.example.testapp.data.remote.dto

import com.example.testapp.domain.model.CategoryItem

data class CategoryDtoItem(
    val addr: Int,
    val categoryId: Int,
    val icon: String,
    val keyword: String,
    val title: String
)
fun CategoryDtoItem.toCategoryItem() = CategoryItem(
    addr = addr,
    categoryId = categoryId,
    icon = icon,
    keyword = keyword,
    title = title
)